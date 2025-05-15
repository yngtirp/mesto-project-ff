// @todo: Темплейт карточки

// @todo: DOM узлы

// @todo: Функция создания карточки

// @todo: Функция удаления карточки

// @todo: Вывести карточки на страницу

const cardList = document.querySelector(".places__list");
const cardTemplate = document.querySelector("#card-template").content;

function addCard(nameValue, linkValue) {
  cardContent = cardTemplate.querySelector(".card").cloneNode(true);
  cardContent.querySelector(".card__title").textContent = nameValue;
  cardContent.querySelector(".card__image").src = linkValue;
  deleteButton = cardContent.querySelector(".card__delete-button");
  cardList.append(cardContent);
  deleteButton.addEventListener("click", function(evt) { 
    const eventTarget = evt.target;
    deleteCard(eventTarget);
  });
}

function deleteCard(deleteButton) { 
  const cardListItem = deleteButton.closest(".card");
  cardListItem.remove();
}

for (let i = 0; i < initialCards.length; i++) {
  addCard(initialCards[i].name, initialCards[i].link);
} 
