
function setRunningStatusPlay() {
	document.getElementById("play").style.display = "inline-block";
	document.getElementById("load").style.display = "none";
	document.getElementById("stop").style.display = "none";
}

function setRunningStatusLoading() {
	document.getElementById("play").style.display = "none";
	document.getElementById("load").style.display = "inline-block";
	document.getElementById("stop").style.display = "none";
}

function setRunningStatusStop() {
	document.getElementById("play").style.display = "none";
	document.getElementById("load").style.display = "none";
	document.getElementById("stop").style.display = "inline-block";
	
	if (window.innerWidth < 1055) {
		document.getElementById("term-side").style.display = "block";
		document.getElementById("code-side").style.display = "none";
	} else {
		document.getElementById("term-side").style.display = "block";
		document.getElementById("code-side").style.display = "block";
	}
}

function switchTerminal() {
	if (window.innerWidth < 1055) {
		if (document.getElementById("term-side").style.display != undefined 
				&& document.getElementById("term-side").style.display != "") {
			var temp = document.getElementById("term-side").style.display;
			document.getElementById("term-side").style.display = document.getElementById("code-side").style.display;
			document.getElementById("code-side").style.display = temp;
		} else {
			document.getElementById("term-side").style.display = "block";
			document.getElementById("code-side").style.display = "none";
		}
	} else {
		document.getElementById("term-side").style.display = "block";
		document.getElementById("code-side").style.display = "block";
	}
}

function hideLoading() {
	document.getElementById("loading").style.display = "none";
}

function closePopup(id) {
	document.getElementById(id).style.display = "none";
}

function openPopup(id) {
	document.getElementById(id).style.display = "block";
}

function download(filename, text) {
	var element = document.createElement("a");
	element.setAttribute("href", "data:text/plain;charset=utf-8,"
			+ encodeURIComponent(text));
	element.setAttribute("download", filename);

	element.style.display = "none";
	document.body.appendChild(element);
	element.click();
	document.body.removeChild(element);
}

function saveSource() {
	download("mypseudoapp.pss", window.editor.getValue());
	closePopup("save");
}

function openFileFromComputer() {
	document.getElementById("file-opener").click();
	closePopup("open");
}

function openFile(e) {
	var file = e.target.files[0];
	if (!file) {
		return;
	}
	var reader = new FileReader();
	reader.onload = function(e) {
		var contents = e.target.result;
		window.editor.setValue(contents);
	};
	reader.readAsText(file);
}

function openSite(site) {
	window.open(site, "_blank");
}