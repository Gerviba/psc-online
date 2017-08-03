var endPointURL = "ws://" + window.location.host + "/console";

var client = null;

function connect() {
	client = new WebSocket(endPointURL);
	client.onmessage = function(event) {
		// console.log(event.data);
		var jsonObj = JSON.parse(event.data);
		resolvePacket(jsonObj);
	};
}

function disconnect() {
	if (client != null) {
		client.close();
		client = null;

		setRunningStatusPlay();
	}
}

function sendPacket(jsonObj) {
	client.send(JSON.stringify(jsonObj));
}

function resolvePacket(packet) {
	if (packet.action == "stdout") {
		printStdOut(packet.message);
	} else if (packet.action == "status") {
		printStatus(packet.message);
	} else if (packet.action == "note") {
		printNote(packet.message);
	} else if (packet.action == "line") {
		printLine(packet.message);
	} else if (packet.action == "terminated") {
		disconnect();
	} else {
		console.error("Invalid packet!");
		console.log(packet);
	}
}

function printStdOut(message) {
	document.getElementById("terminal").innerHTML += "<span>"
			+ message.replace("\n", "<br />") + "</span>";
	terminalBottom();
}

function printStatus(message) {
	document.getElementById("terminal").innerHTML += "<span class=\"status\">"
			+ message.replace("\n", "<br />") + "</span>";
	terminalBottom();
}

function printNote(message) {
	document.getElementById("terminal").innerHTML += "<span class=\"note\"># "
			+ message.replace("\n", "<br />") + "</span>";
	terminalBottom();
}

function printLine(message) {
	document.getElementById("terminal").innerHTML += "<span class=\"line\">&gt;_ "
			+ message.replace("\n", "<br />") + "</span>";
	terminalBottom();
}

function terminalBottom() {
	var term = document.getElementById("terminal");
	term.scrollTop = term.scrollHeight;
}

function start() {
	disconnect();
	connect();
	setRunningStatusLoading();

	waitForSocketConnection(client, function() {
		setRunningStatusStop();

		sendPacket({
			action : "start",
			code : btoa(unescape(encodeURIComponent(window.editor.getValue())))
		});
	});
}

function stop() {
	setRunningStatusLoading();
	disconnect();
}

function waitForSocketConnection(socket, callback) {
	setTimeout(function() {
		if (socket.readyState === 1) {
			if (callback != null)
				callback();
			return;
		} else {
			waitForSocketConnection(socket, callback);
		}
	}, 5);
}

function sendConsole() {
	if (client == null) {
		document.getElementById("line").value = "";
		return;
	}

	sendPacket({
		action : "write",
		line : btoa(unescape(encodeURIComponent(document.getElementById("line").value)))
	});
	document.getElementById("line").value = "";
}
