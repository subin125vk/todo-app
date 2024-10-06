function confirmAction(message) {
    return confirm(message);
}

function toggleDescription(todoId) {
    const descriptionField = document.getElementById(`todoDescription-${todoId}`);
    if (descriptionField) {
        if (descriptionField.style.display === "none" || descriptionField.style.display === "") {
            descriptionField.style.display = "block"; 
        } else {
            descriptionField.style.display = "none";
        }
    }
}

function showExportMessage() {
    alert('Project exported successfully as Markdown!');
    return true; 
}
