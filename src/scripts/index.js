import '../index.css';
import {initialCards} from './cards.js';
import {createCard, addCard, deleteCard, likeButtonState} from "./card.js";
import {showModal, closeModal, ModalListenerClick, ModalListenerKeyboard, handleFormSubmit, addCardFormSubmit, showPicturePopup, openedModal, setOpenedModal} from "./modal.js";

export const cardList = document.querySelector(".places__list");
export const cardTemplate = document.querySelector("#card-template").content;
const editProfileButton = document.querySelector(".profile__edit-button");
const addCardButton = document.querySelector(".profile__add-button")
const editProfileForm = document.forms.edit_profile;
export const personName = editProfileForm.elements.name;
export const personDescription = editProfileForm.elements.description;
export const addCardForm = document.forms.new_place;
export const placeName = addCardForm.elements.place_name;
export const placeLink = addCardForm.elements.link;

initialCards.forEach((elem) => {
  const card = createCard(elem.name, elem.link, deleteCard, likeButtonState, showPicturePopup);
  addCard(card);
})

editProfileButton.addEventListener("click", (evt) => { 
  setOpenedModal(document.querySelector(".popup_type_edit"))
  showModal(openedModal);
})

addCardButton.addEventListener("click", (evt) => { 
  setOpenedModal(document.querySelector(".popup_type_new-card"));
  showModal(openedModal);
})

editProfileForm.addEventListener("submit", handleFormSubmit);

addCardForm.addEventListener("submit", addCardFormSubmit);

