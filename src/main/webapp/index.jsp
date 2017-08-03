<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>PSeudoCompiler-Online</title>
        <meta charset="UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
        <meta name="author" content="Szabó Gergely (Gerviba)"/>
        <meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, width=device-width"/>
        <meta name="msapplication-tap-highlight" content="no"/>
        <link rel="icon" href="style/icon_x16.png"/>
        
        <link href="https://fonts.googleapis.com/css?family=Roboto+Mono:300,400,700" rel="stylesheet">
        <link rel="stylesheet" href="codemirror/lib/codemirror.css">
        <link href="style/style.css" rel="stylesheet" type="text/css" media="all"/>
        <link rel="stylesheet" href="style/font-awesome.min.css">

        <script src="codemirror/lib/codemirror.js"></script>
        <script src="codemirror/mode/pseudo/pseudo.js"></script>
        <script src="js/script.js"></script>
        <script src="js/connection.js"></script>
        <script src="js/ui.js"></script>
	</head>
	<body onunload="disconnect()">
		<div class="loading" id="loading"></div>
		
		<div class="popup open" id="open">
			<h2>Open program<span onclick="closePopup('open')"><i class="fa fa-times-circle-o" aria-hidden="true"></i></span></h2>
			<div class="popup-entry" onclick="openFileFromComputer()">
				<i class="fa fa-file-code-o" aria-hidden="true"></i>
				<h3>Source file</h3>
				Open any '.pss' file from your computer.
			</div>
			<div class="popup-entry disabled">
				<i class="fa fa-hdd-o" aria-hidden="true"></i>
				<h3>Local Browser Storage</h3>
				Open saved codes from your browser's local storage.
			</div>
			<div class="popup-entry disabled">
				<i class="fa fa-code" aria-hidden="true"></i>
				<h3>Examples</h3>
				Open premade examples.
			</div>
		</div>
		
		<div class="popup save" id="save">
			<h2>Save program<span onclick="closePopup('save')"><i class="fa fa-times-circle-o" aria-hidden="true"></i></span></h2>
			<div class="popup-entry" onclick="saveSource()">
				<i class="fa fa-file-code-o" aria-hidden="true"></i>
				<h3>Source file</h3>
				Your source code will be saved to a '.pss' file.
			</div>
			<div class="popup-entry disabled">
				<i class="fa fa-file-o" aria-hidden="true"></i>
				<h3>Compiled</h3>
				Your code will be compiled and saved to a '.psc' executable file.
			</div>
			<div class="popup-entry disabled">
				<i class="fa fa-hdd-o" aria-hidden="true"></i>
				<h3>Local Browser Storage</h3>
				Your source code will be saved to the browsers local storage.
			</div>
			<div class="popup-entry disabled">
				<i class="fa fa-cloud-upload" aria-hidden="true"></i>
				<h3>Cloud Snippet</h3>
				Your source code will be saved to the server.
			</div>
		</div>
		
		<div class="popup about" id="about">
			<h2>About<span onclick="closePopup('about')"><i class="fa fa-times-circle-o" aria-hidden="true"></i></span></h2>
			<img src="style/icon_x96.png" height="96" width="96" alt="PSC Logo" title="PSeudoCode Logo" />
			<h3>PSeudoCode-Online</h3>
			<p>Created by <a href="https://github.com/Gerviba/" target="_blank">Szabó Gergely (Gerviba)</a></p>
			<p>Licensed under <a href="https://www.gnu.org/licenses/gpl-3.0.en.html" target="_blank">GNU/GPL 3.0</a></p>
			<i class="fa fa-github" aria-hidden="true"></i> <a href="https://github.com/Gerviba/psc-online/" target="_blank">PSeudoCode-Online on GitHub</a><br />
			<i class="fa fa-github" aria-hidden="true"></i> <a href="https://github.com/Gerviba/psc-compiler/" target="_blank">PSeudoCode-Compiler on GitHub</a>
			
			<h3>Used APIs</h3>
			<div class="half">
				<h4>CodeMirror</h4>
				<p>JS based syntax highlighter by Marijn Haverbeke and others (<a href="https://github.com/codemirror/CodeMirror/blob/master/LICENSE" target="_blank">MIT license</a>)</p>
				<i class="fa fa-home" aria-hidden="true"></i> <a href="http://codemirror.net/" target="_blank">codemirror.net</a><br />
				<i class="fa fa-github" aria-hidden="true"></i> <a href="http://codemirror.net/" target="_blank">Codemirror on GitHub</a>
			</div>
			<div class="half">
				<h4>Font Awesome</h4>
				<p>Cool icons by Dave Gandy (Font: <a href="http://scripts.sil.org/cms/scripts/page.php?site_id=nrsi&id=OFL" target="_blank">SIL OFL 1.1 license</a> 
				   and CSS: <a href="https://opensource.org/licenses/mit-license.html" target="_blank">MIT license</a>)</p>
				<i class="fa fa-home" aria-hidden="true"></i> <a href="http://fontawesome.io" target="_blank">fontawesome.io</a><br />
				<i class="fa fa-github" aria-hidden="true"></i> <a href="https://github.com/FortAwesome/Font-Awesome/" target="_blank">Font Awesome on GitHub</a>
			</div>
		</div>
		
		<div class="header">
		    <span class="logo">PSeudoCode</span>
		    <!--span class="menu"><i class="fa fa-plus" aria-hidden="true"></i>Add</span-->
	        <span class="menu" onclick="openPopup('open');closePopup('save');closePopup('about')"><i class="fa fa-folder-open" aria-hidden="true"></i>Open</span>
		    <span class="menu" onclick="openPopup('save');closePopup('open');closePopup('about')"><i class="fa fa-floppy-o" aria-hidden="true"></i>Save</span>
		    <span class="menu" id="play" onclick="start()"><i class="fa fa-play" aria-hidden="true"></i>Run</span>
		    <span class="menu" id="load"><i class="fa fa-circle-o-notch fa-spin"></i>...</span>
		    <span class="menu" id="stop" onclick="stop()"><i class="fa fa-stop" aria-hidden="true"></i>Stop</span>
		    <span class="menu" onclick="openSite('https://github.com/gerviba/psc-online/')"><i class="fa fa-code-fork" aria-hidden="true"></i>Git</span>
            <span class="menu" onclick="openPopup('about');closePopup('save');closePopup('open')"><i class="fa fa-info" aria-hidden="true"></i>About</span>
            <span class="menu" onclick="openSite('https://github.com/Gerviba/psc-compiler/blob/master/rules.md')"><i class="fa fa-book" aria-hidden="true"></i>Manual</span>
			<span class="menu switch-terminal" onclick="switchTerminal()"><i class="fa fa-terminal" aria-hidden="true"></i></span>
		</div>
		<div class="content">
			<div class="code" id="code-side">
<form><textarea id="code" name="code">
#DEBUG OFF
#AUTHOR Gerviba
#VERSION 1.0
#DESCRIPTION HelloWord

Program HellowWord_minSort
    // Változók
    a := (10, 210, 213, 312, -1, -30, 230, 500, 0, 1001)
    n := 10
    
    // Rendezés
    Ciklus i := 1-től n-1-ig
        min := i
        Ciklus j := i + 1-től n-ig
            Ha a[min] > a[j] akkor
                min := j
            elág. vége
        Ciklus vége
        
        temp := a[min]
        a[min] := a[i]
        a[i] := temp
    Ciklus vége
    
    // Kiírás
	KI: "Rendezett:"
    Ciklus i := 1-től n-ig
        KI: a[i]
    Ciklus vége
Program vége
</textarea></form>
			</div>
			<div class="terminal" id="term-side">
			    <div class="result" id="terminal">
    			    <span class="note"># PSeudoCode-Online Console v1.0.803</span>
    			    <span class="note"># Compiler: PSeudoCode-Compiler v1.0.803</span>
    			    <span class="note"># Host: Default Testing Host</span><br />
			    	<span></span>
			    </div>

                <form onsubmit="sendConsole(); return false">
                    <i class="fa fa-terminal" aria-hidden="true"></i><input type="text" name="line" id="line" spellcheck="false" autocomplete="off" />
                </form>
			</div>
		</div>
		<input type="file" name="file-opener" id="file-opener" class="hide-me" />
		
	    <script>
            window.onload = function() {
                window.editor = CodeMirror.fromTextArea(document.getElementById("code"), {
                    indentWithTabs: true,
                    smartIndent: true,
                    lineNumbers: true,
                    matchBrackets: true,
                    autofocus: true,
                    extraKeys: {"Ctrl-Space": "autocomplete"},
                    hintOptions: {tables: {
                        users: {name: null, score: null, birthDate: null},
                        countries: {name: null, population: null, size: null}
                    }},
                    mode: "text/pseudocode"
                });

                document.getElementById("file-opener").addEventListener("change", openFile, false);
                if (window.innerWidth < 1055) {
            		document.getElementById("term-side").style.display = "none";
            		document.getElementById("code-side").style.display = "block";
                }
                
                hideLoading();
            }
        </script>

	</body>
</html>