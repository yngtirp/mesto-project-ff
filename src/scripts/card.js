import { cardList, cardTemplate } from "./index.js";

export const createCard = (nameValue, linkValue, deleteCard, likeButtonState, showPicturePopup) => {
  const card = cardTemplate.querySelector(".card").cloneNode(true);
  const deleteButton = card.querySelector(".card__delete-button");
  const likeButton = card.querySelector(".card__like-button");
  const cardImage = card.querySelector(".card__image");

  card.querySelector(".card__title").textContent = nameValue;

  cardImage.src = linkValue;
  cardImage.alt = `На фото: ${nameValue}`;

  deleteButton.addEventListener("click", () => deleteCard(card));
  likeButton.addEventListener("click", () => likeButtonState(likeButton));
  cardImage.addEventListener("click", (evt) => showPicturePopup(evt, nameValue));
  
  return card;
}

export const addCard = (card) => { 
  cardList.append(card);
}

export const deleteCard = (cardListItem) => { 
  cardListItem.remove();
}

export const likeButtonState = (target) => {
  target.classList.toggle("card__like-button_is-active");
}