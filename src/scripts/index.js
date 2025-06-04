import '../index.css';
import {initialCards} from './cards.js';
import {createCard, deleteCard, likeButtonState} from "./card.js";
import {showModal, closeModal} from "./modal.js";

const cardList = document.querySelector(".places__list");
const profileTile = document.querySelector(".profile__title");
const profileDesciption = document.querySelector(".profile__description");
const addCardForm = document.forms.new_place;
const placeName = addCardForm.elements.place_name;
const placeLink = addCardForm.elements.link;
const editProfileButton = document.querySelector(".profile__edit-button");
const addCardButton = document.querySelector(".profile__add-button")
const editProfileForm = document.forms.edit_profile;
const personName = editProfileForm.elements.name;
const personDescription = editProfileForm.elements.description;
const newCardModal = document.querySelector(".popup_type_new-card");
const editProfileModal = document.querySelector(".popup_type_edit");
const popupTypeImage = document.querySelector(".popup_type_image");
const imgShowPopup = document.querySelector(".popup__image")
const imgDescriptionShowPopup = document.querySelector(".popup__caption");

const addCard = (card, cardList) => { 
  cardList.append(card);
}

const editInputEditProfile = () => { 
  personName.value = profileTile.textContent;
  personDescription.value = profileDesciption.textContent;
}

const EditProfileFormSubmit = (evt) => { 
  evt.preventDefault();
  profileTile.textContent = personName.value;
  profileDesciption.textContent = personDescription.value;
  closeModal(editProfileModal);
}

const addCardFormSubmit = (evt) => { 
  evt.preventDefault();
  const newCard = createCard(placeName.value, placeLink.value, deleteCard, likeButtonState, showPicturePopup);
  cardList.prepend(newCard);
  addCardForm.reset();
  closeModal(newCardModal);
}

const showPicturePopup = (evt, nameValue) => { 
  imgShowPopup.src = evt.target.src;
  imgShowPopup.alt = evt.target.alt;
  imgDescriptionShowPopup.textContent = nameValue;
  showModal(popupTypeImage);
}

initialCards.forEach((elem) => {
  const card = createCard(elem.name, elem.link, deleteCard, likeButtonState, showPicturePopup);
  addCard(card, cardList);
})

editProfileButton.addEventListener("click", () => { 
  editInputEditProfile();
  showModal(editProfileModal);
})

addCardButton.addEventListener("click", () => { 
  showModal(newCardModal);
})

editProfileForm.addEventListener("submit", EditProfileFormSubmit);

addCardForm.addEventListener("submit", addCardFormSubmit)

