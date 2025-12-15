import "../index.css";
import {createCard, deleteCard, likeButtonState} from "./card.js";
import {showModal, closeModal} from "./modal.js";
import {enableValidation, clearValidation} from "./validation.js"
import {getProfileInfo, getCards, updateProfileInfo, addNewCardOnServer, updateAvatar, deleteCardFromServer, likeCard, unlikeCard} from "./api.js"

const cardList = document.querySelector(".places__list");
const profileTitle = document.querySelector(".profile__title");
const profileDesciption = document.querySelector(".profile__description");
const profileAvatar = document.querySelector(".profile__image")
const addCardForm = document.forms.new_place;
const placeName = addCardForm.elements.place_name;
const placeLink = addCardForm.elements.link;
const addCardFormSubmitButton = addCardForm.elements.new_place_form_submit_button;
const updateAvatarForm = document.forms.new_avatar;
const updateAvatarSubmitButton = updateAvatarForm.elements.new_avatar_form_submit_button
const placeLinkAvatar = updateAvatarForm.elements.new_avatar_link;
const editProfileButton = document.querySelector(".profile__edit-button");
const addCardButton = document.querySelector(".profile__add-button")
const editProfileForm = document.forms.edit_profile;
const personName = editProfileForm.elements.name;
const personDescription = editProfileForm.elements.description;
const editProfileFormSubmitButton = editProfileForm.elements.edit_profile_submit_button;
const newCardModal = document.querySelector(".popup_type_new-card");
const editProfileModal = document.querySelector(".popup_type_edit");
const updateAvatarModal = document.querySelector(".popup_type_update-avatar");
const popupTypeImage = document.querySelector(".popup_type_image");
const imgShowPopup = document.querySelector(".popup__image")
const imgDescriptionShowPopup = document.querySelector(".popup__caption");
const updateAvatarButton = document.querySelector(".profile__image")
let myId = null;
const config = {
  baseUrl: 'http://localhost:3000',
  headers: {
    authorization: "b2069597-819f-48e3-8235-452d763c5957",
    'Content-Type': 'application/json'
  }
}
const validationConfig = {
  formSelector: ".popup__form",
  inputSelector: ".popup__input",
  submitButtonSelector: ".popup__button",
  inactiveButtonClass: "button_inactive",
  inputErrorClass: "form__input_type_error",
  errorClass: "form__input-error_active"
};

const addCard = (card, cardList) => { 
  cardList.append(card);
}

const editInputEditProfile = () => {
  personName.value = profileTitle.textContent;
  personDescription.value = profileDesciption.textContent;
  clearValidation(editProfileModal, validationConfig);
}

const editProfileFormSubmit = (evt) => { 
  evt.preventDefault();
  editProfileFormSubmitButton.textContent = "Сохранение...";
  updateProfileInfo(config, personName.value, personDescription.value)
    .then((data) => {
      profileTitle.textContent = data.name;
      profileDesciption.textContent = data.about;
      closeModal(editProfileModal);
    })
    .catch(err => console.log(err))
    .finally(() => { 
      editProfileFormSubmitButton.textContent = "Сохранить";
    });
};


const addCardFormSubmit = (evt) => { 
  evt.preventDefault();
  addCardFormSubmitButton.textContent = "Сохранение...";
  addNewCardOnServer(config, placeName.value, placeLink.value)
    .then((cardInfoObject) => {
      const card = createCard(cardInfoObject.name, cardInfoObject.link, 
        deleteCard, likeButtonState, showPicturePopup, 
        showLikes(cardInfoObject.likes.length), 
        config, cardInfoObject._id, cardInfoObject.likes, 
        cardInfoObject.owner._id, myId, deleteCardFromServer, likeCard, unlikeCard);
      cardList.prepend(card);
      clearValidation(newCardModal, validationConfig);
      closeModal(newCardModal);
      addCardForm.reset();
    })
    .catch(err => console.log(err))
    .finally(() => { 
      addCardFormSubmitButton.textContent = "Сохранить";
    });
}

const updateAvatarFormSubmit = (evt) => { 
  evt.preventDefault();
  updateAvatarSubmitButton.textContent = "Сохранение...";
  updateAvatar(config, placeLinkAvatar.value)
  .then((res) => { 
    profileAvatar.style.backgroundImage = `url(${res.avatar})`;  
      closeModal(updateAvatarModal);
      updateAvatarForm.reset(); 
      clearValidation(updateAvatarModal, validationConfig);
  })
  .catch(err => console.log(err))
  .finally(() => { 
    updateAvatarSubmitButton.textContent = "Сохранить";
  });
}

const showPicturePopup = (evt, nameValue) => { 
  imgShowPopup.src = evt.target.src;
  imgShowPopup.alt = evt.target.alt;
  imgDescriptionShowPopup.textContent = nameValue;
  showModal(popupTypeImage);
}

editProfileButton.addEventListener("click", () => { 
  editInputEditProfile();
  showModal(editProfileModal);
})

addCardButton.addEventListener("click", () => { 
  showModal(newCardModal);
})

updateAvatarButton.addEventListener("click", () =>{
  showModal(updateAvatarModal);
})

editProfileForm.addEventListener("submit", editProfileFormSubmit);

addCardForm.addEventListener("submit", addCardFormSubmit);

updateAvatarForm.addEventListener("submit", updateAvatarFormSubmit)

enableValidation(validationConfig);

const showLikes = (currentLikes) => currentLikes || "";

Promise.all([getProfileInfo(config), getCards(config)])
.then(([userData, cards]) => {
  myId = userData._id;
  cards.forEach((cardInfoObject) => {
    const card = createCard(cardInfoObject.name, cardInfoObject.link, 
      deleteCard, likeButtonState, showPicturePopup, 
      showLikes(cardInfoObject.likes.length), config, 
      cardInfoObject._id, cardInfoObject.likes, cardInfoObject.owner._id, myId,
      deleteCardFromServer, likeCard, unlikeCard);
      addCard(card, cardList);
    }) 
  profileTitle.textContent = userData.name;
  profileDesciption.textContent = userData.about;
  profileAvatar.style.backgroundImage = `url(${userData.avatar})`;
})
.catch(err => console.log(err));
