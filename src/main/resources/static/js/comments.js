let recipeId = document.getElementById("recipeId").getAttribute("value");
let commentSection = document.getElementById("commentCtnr");

function fetchComments() {
    fetch(`http://localhost:8080/api/${recipeId}/comments`)
        .then((response) => response.json())
        .then((body) => {
            commentSection.innerHTML = '';
            for (comment of body) {
                let commentHtml = '<div class="comments">\n';
                commentHtml += '<div hidden>' + comment.id + '</div>';
                commentHtml += '<h4>' + comment.author.username + '</h4>';
                commentHtml += '<p>' + comment.text + '</p>';
                commentHtml += '<span>' + comment.dateTimePost + '</span>';
                commentHtml += '</div>\n';

                commentSection.innerHTML += commentHtml;
            }
        });
}

fetchComments();

const csrfHeaderName = document.getElementById('csrf').getAttribute('name');
const csrfHeaderValue = document.getElementById('csrf').getAttribute('value');

let commentForm = document.getElementById("commentForm");
commentForm.addEventListener("submit", (event) => {
    event.preventDefault();

    let text = document.getElementById("text").value;

    fetch(`http://localhost:8080/api/${recipeId}/comments`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        },
        body: JSON.stringify({
            text: text
        })
    })
        .then((res) => {
            console.log(res);
            fetchComments();
            document.getElementById("text").value = '';
        });
});