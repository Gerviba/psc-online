// Created by Szabó Gergely (Gerviba)
// using sql.js by Marijn Haverbeke and others

(function(mod) {
  if (typeof exports == "object" && typeof module == "object") // CommonJS
    mod(require("../../lib/codemirror"));
  else if (typeof define == "function" && define.amd) // AMD
    define(["../../lib/codemirror"], mod);
  else // Plain browser env
    mod(CodeMirror);
})(function(CodeMirror) {
"use strict";

  CodeMirror.defineMode("pseudo", function(config, parserConfig) {
    "use strict";
  
    var atoms          = parserConfig.atoms || {"false": true, "true": true, "null": true},
        io             = parserConfig.io || {},
        keywords       = parserConfig.keywords || {},
        operatorChars  = parserConfig.operatorChars || /^[*+\-%<>!=&|~^]/;
  
    function tokenBase(stream, state) {
      var ch = stream.next();
      
      if (ch.charCodeAt(0) > 47 && ch.charCodeAt(0) < 58) {
        stream.match(/^[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)?/);
        stream.eat('.');
        return "number";
      } else if (ch == '"') {
        state.tokenize = tokenLiteral(ch);
        return state.tokenize(stream, state);
      } else if (/^[\(\),\;\[\]]/.test(ch)) {
        return null;
      } else if (ch == "/" && stream.eat("/")) {
        stream.skipToEnd();
        return "comment";
      } else if (ch == "#") {
        stream.skipToEnd();
        return "directive";
      } else if (ch == "/" && stream.eat("*")) {
        state.tokenize = tokenComment;
        return state.tokenize(stream, state);
      } else if (operatorChars.test(ch)) {
        // operators
        stream.eatWhile(operatorChars);
        return null;
      } else {
        stream.eatWhile(/^[_\w\dÁáÉéÍíÓóÖöŐőÚúÜüŰű]/);
        var word = stream.current().toLowerCase();
  
        if (atoms.hasOwnProperty(word)) return "atom";
        if (io.hasOwnProperty(word)) return "io";
        if (keywords.hasOwnProperty(word)) return "keyword";
        return null;
      }
    }
  
    function tokenLiteral(quote) {
      return function(stream, state) {
        var escaped = false, ch;
        while ((ch = stream.next()) != null) {
          if (ch == quote && !escaped) {
            state.tokenize = tokenBase;
            break;
          }
          escaped = !escaped && ch == "\\";
        }
        return "string";
      };
    }
    function tokenComment(stream, state) {
      while (true) {
        if (stream.skipTo("*")) {
          stream.next();
          if (stream.eat("/")) {
            state.tokenize = tokenBase;
            break;
          }
        } else {
          stream.skipToEnd();
          break;
        }
      }
      return "comment";
    }
  
    function pushContext(stream, state, type) {
      state.context = {
        prev: state.context,
        indent: stream.indentation(),
        col: stream.column(),
        type: type
      };
    }
  
    function popContext(state) {
      state.indent = state.context.indent;
      state.context = state.context.prev;
    }
  
    return {
      startState: function() {
        return {tokenize: tokenBase, context: null};
      },
  
      token: function(stream, state) {
        if (stream.sol()) {
          if (state.context && state.context.align == null)
            state.context.align = false;
        }
        if (stream.eatSpace()) return null;
  
        var style = state.tokenize(stream, state);
        if (style == "comment") return style;
  
        if (state.context && state.context.align == null)
          state.context.align = true;
  
        var tok = stream.current();
        if (tok == "(")
          pushContext(stream, state, ")");
        else if (tok == "[")
          pushContext(stream, state, "]");
        else if (state.context && state.context.type == tok)
          popContext(state);
        return style;
      },
  
      indent: function(state, textAfter) {
        var cx = state.context;
        if (!cx) return CodeMirror.Pass;
        var closing = textAfter.charAt(0) == cx.type;
        if (cx.align) return cx.col + (closing ? 0 : 1);
        else return cx.indent + (closing ? 0 : config.indentUnit);
      },
  
      blockCommentStart: "/*",
      blockCommentEnd: "*/"
    };
  });
  
  (function() {
    "use strict";
  
    function set(str) {
      var obj = {}, words = str.split(" ");
      for (var i = 0; i < words.length; ++i) obj[words[i]] = true;
      return obj;
    }
    
    CodeMirror.defineMIME("text/pseudocode", {
      name: "pseudo",
      keywords: set("program eljárás eljarás eljáras eljaras funkció funkcio function method metódus metodus elj vege vége v ciklus ha akkor különben kulonben künonben amúgy amugy kulönben amég ameg ameddig elágazás elagazas elagazás elágazas elág elag el től tól ig egyesével"),
      io: set("ki be in out"),
      atoms: set("false true null unknown igaz hamis igen nem"),
      operatorChars: /^[*+\-%<>!=&|^]/,
  
    });
    
  }());

});