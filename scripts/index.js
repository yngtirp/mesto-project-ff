const cardList = document.querySelector(".places__list");
const cardTemplate = document.querySelector("#card-template").content;

const createCard = (nameValue, linkValue, deleteCard) => {
  const card = cardTemplate.querySelector(".card").cloneNode(true);
  const deleteButton = card.querySelector(".card__delete-button");

  card.querySelector(".card__title").textContent = nameValue;

  card.querySelector(".card__image").src = linkValue;
  card.querySelector(".card__image").alt = `На фото: ${nameValue}`;

  deleteButton.addEventListener("click", () => { deleteCard(card) });

  return card;
}

const addCard = (card) => { 
  cardList.append(card);
}

const deleteCard = (cardListItem) => { 
  cardListItem.remove();
}

initialCards.forEach((elem) => {
  const card = createCard(elem.name, elem.link, deleteCard);
  addCard(card);
})