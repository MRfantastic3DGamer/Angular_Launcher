import ace

function getCode(){
    var editor = ace.edit("editor")
    let code = editor.getValue()
    return code
}

function setCodeTest(){
    var editor = ace.edit("editor")
    editor.setValue("new code")
}