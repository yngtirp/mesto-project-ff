import { personName, personDescription, placeName, placeLink, cardList, addCardForm} from "./index.js";
import {createCard, deleteCard, likeButtonState} from "./card.js";
let openedModal;

const setOpenedModal = (target) => { 
  openedModal = target;
}

export const showModal = (target) => { 
  if (target === document.querySelector(".popup_type_edit")) { 
    personName.value = document.querySelector(".profile__title").textContent;
    personDescription.value = document.querySelector(".profile__description").textContent;
  }
  setOpenedModal(target);
  target.classList.add("popup_is-opened");
  target.addEventListener("click", modalListenerClick);
  document.addEventListener("keydown", modalListenerKeyboard);
}

export const closeModal = (target) => { 
  target.classList.remove("popup_is-opened");
  target.removeEventListener("click", modalListenerClick);
  target.parentElement.removeEventListener("keydown", modalListenerKeyboard);
}

const modalListenerClick = (evt) => { 
  if (evt.target.classList.contains("popup") || evt.target.classList.contains("popup__close")) {
    closeModal(openedModal);
  }
}

const modalListenerKeyboard = (evt) => { 
  if (evt.key === "Escape") { 
    closeModal(openedModal);
  }
} 

export const handleFormSubmit = (evt) => { 
  evt.preventDefault();
  document.querySelector(".profile__title").textContent = personName.value;
  document.querySelector(".profile__description").textContent = personDescription.value;
  closeModal(openedModal);
}

export const addCardFormSubmit = (evt) => { 
  evt.preventDefault();
  const newCard = createCard(placeName.value, placeLink.value, deleteCard, likeButtonState, showPicturePopup);
  cardList.prepend(newCard);
  addCardForm.reset();
  closeModal(openedModal);
}

export const showPicturePopup = (evt, nameValue) => { 
  setOpenedModal(document.querySelector(".popup_type_image"));
  const imgShowPopup = document.querySelector(".popup__image");
  const imgDescriptionShowPopup = document.querySelector(".popup__caption");
  imgShowPopup.src = evt.target.src;
  imgShowPopup.alt = evt.target.alt;
  imgDescriptionShowPopup.textContent = nameValue;
  showModal(openedModal);
}