import "../index.css";
import {createCard, deleteCard, likeButtonState} from "./card.js";
import {showModal, closeModal} from "./modal.js";
import {enableValidation, clearValidation} from "./validation.js"
import {getProfileInfo, getCards, updateProfileInfo, addNewCardOnServer, updateAvatar, deleteCardFromServer, likeCard, unlikeCard, signup, signin} from "./api.js"

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
const signinModal = document.querySelector(".popup_type_signin");
const signupModal = document.querySelector(".popup_type_signup");
const logoutModal = document.querySelector(".popup_type_logout");
const signinForm = document.forms.signin;
const signupForm = document.forms.signup;
const switchToSignup = document.getElementById("switch-to-signup");
const switchToSignin = document.getElementById("switch-to-signin");
const profileSection = document.getElementById("profile-section");
const authSection = document.getElementById("auth-section");
const headerAuthButton = document.getElementById("header-auth-button");
const headerLogoutButton = document.getElementById("header-logout-button");
const signinError = document.getElementById("signin-error");
const signupError = document.getElementById("signup-error");

let myId = null;
let config = {
  baseUrl: 'http://localhost:3000',
  headers: {
    authorization: localStorage.getItem('token') || '',
    'Content-Type': 'application/json'
  }
}

function updateConfig(token) {
  config.headers.authorization = token;
  localStorage.setItem('token', token);
}

function checkAuth() {
  const token = localStorage.getItem('token');
  if (!token) {
    showAuthSection();
    return false;
  }
  updateConfig(token);
  showProfileSection();
  return true;
}

// Show/hide sections based on auth
function showProfileSection() {
  if (profileSection) {
    profileSection.style.display = 'flex';
  }
  if (headerAuthButton) {
    headerAuthButton.style.display = 'none';
  }
  if (headerLogoutButton) {
    headerLogoutButton.style.display = 'block';
  }
  if (addCardButton) {
    addCardButton.disabled = false;
  }
}

function showAuthSection() {
  if (profileSection) {
    profileSection.style.display = 'none';
  }
  if (headerAuthButton) {
    headerAuthButton.style.display = 'block';
  }
  if (headerLogoutButton) {
    headerLogoutButton.style.display = 'none';
  }
  // Disable add card button when not authorized
  if (addCardButton) {
    addCardButton.disabled = true;
  }
}

function showError(errorElement, message) {
  if (errorElement) {
    errorElement.textContent = message;
    errorElement.style.display = 'block';
  }
}

function hideError(errorElement) {
  if (errorElement) {
    errorElement.style.display = 'none';
    errorElement.textContent = '';
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
  if (config.headers.authorization) {
    showModal(newCardModal);
  }
})

updateAvatarButton.addEventListener("click", () =>{
  showModal(updateAvatarModal);
})

editProfileForm.addEventListener("submit", editProfileFormSubmit);

addCardForm.addEventListener("submit", addCardFormSubmit);

updateAvatarForm.addEventListener("submit", updateAvatarFormSubmit)

enableValidation(validationConfig);

const showLikes = (currentLikes) => currentLikes || "";

// Load cards (always, even without auth)
function loadCards() {
  // Try to get cards without auth first
  const cardsConfig = {
    baseUrl: config.baseUrl,
    headers: {
      'Content-Type': 'application/json'
    }
  };
  
  getCards(cardsConfig)
    .then((cards) => {
      cardList.innerHTML = '';
      cards.forEach((cardInfoObject) => {
        const isAuthorized = !!config.headers.authorization;
        const card = createCard(
          cardInfoObject.name, 
          cardInfoObject.link, 
          deleteCard, 
          likeButtonState, 
          showPicturePopup, 
          showLikes(cardInfoObject.likes.length), 
          isAuthorized ? config : null, // Pass null config if not authorized
          cardInfoObject._id, 
          cardInfoObject.likes, 
          cardInfoObject.owner._id, 
          myId,
          deleteCardFromServer, 
          likeCard, 
          unlikeCard,
          isAuthorized
        );
        addCard(card, cardList);
      });
    })
    .catch(err => {
      console.log("Error loading cards:", err);
    });
}

// Load app data (profile + cards)
function loadApp() {
  const token = localStorage.getItem('token');
  if (!token) {
    loadCards();
    showAuthSection();
    return;
  }

  Promise.all([getProfileInfo(config), getCards(config)])
    .then(([userData, cards]) => {
      myId = userData._id;
      cardList.innerHTML = '';
      cards.forEach((cardInfoObject) => {
        const card = createCard(
          cardInfoObject.name, 
          cardInfoObject.link, 
          deleteCard, 
          likeButtonState, 
          showPicturePopup, 
          showLikes(cardInfoObject.likes.length), 
          config, 
          cardInfoObject._id, 
          cardInfoObject.likes, 
          cardInfoObject.owner._id, 
          myId,
          deleteCardFromServer, 
          likeCard, 
          unlikeCard,
          true
        );
        addCard(card, cardList);
      });
      profileTitle.textContent = userData.name;
      profileDesciption.textContent = userData.about || '';
      const avatarUrl = userData.avatar || 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnXqIkcn-r6Mzz93BpSx5qTEcYB-SlZPcPSg&s';
      profileAvatar.style.backgroundImage = `url(${avatarUrl})`;
      showProfileSection();
    })
    .catch(err => {
      console.log(err);
      if (err.includes('401') || err.includes('403')) {
        localStorage.removeItem('token');
        config.headers.authorization = '';
        loadCards();
        showAuthSection();
      }
    });
}

const signinFormSubmit = (evt) => {
  evt.preventDefault();
  hideError(signinError);
  const submitButton = signinForm.elements.signin_submit_button;
  submitButton.textContent = "Вход...";
  signin(config, signinForm.elements.email.value, signinForm.elements.password.value)
    .then((data) => {
      if (data.error) {
        showError(signinError, "Неправильный логин или пароль, попробуйте снова");
        submitButton.textContent = "Войти";
        return;
      }
      updateConfig(data.authorization);
      closeModal(signinModal);
      signinForm.reset();
      hideError(signinError);
      showProfileSection();
      loadApp();
    })
    .catch(err => {
      showError(signinError, "Неправильный логин или пароль, попробуйте снова");
      submitButton.textContent = "Войти";
    });
};

// Signup handler
const signupFormSubmit = (evt) => {
  evt.preventDefault();
  hideError(signupError);
  const submitButton = signupForm.elements.signup_submit_button;
  submitButton.textContent = "Регистрация...";
  const about = signupForm.elements.about ? signupForm.elements.about.value : '';
  const avatar = signupForm.elements.avatar ? signupForm.elements.avatar.value : '';
  signup(config, signupForm.elements.email.value, signupForm.elements.password.value, 
    signupForm.elements.name.value, about, avatar)
    .then((data) => {
      if (data.error) {
        showError(signupError, data.error === "User already exists" ? "Пользователь с таким email уже существует" : data.error);
        submitButton.textContent = "Зарегистрироваться";
        return;
      }
      updateConfig(data.authorization);
      closeModal(signupModal);
      signupForm.reset();
      hideError(signupError);
      showProfileSection();
      loadApp();
    })
    .catch(err => {
      showError(signupError, "Ошибка регистрации. Попробуйте снова.");
      submitButton.textContent = "Зарегистрироваться";
    });
};

switchToSignup.addEventListener("click", (e) => {
  e.preventDefault();
  hideError(signinError);
  closeModal(signinModal);
  showModal(signupModal);
});

switchToSignin.addEventListener("click", (e) => {
  e.preventDefault();
  hideError(signupError);
  closeModal(signupModal);
  showModal(signinModal);
});

const handleLogout = () => {
  localStorage.removeItem('token');
  config.headers.authorization = '';
  myId = null;
  showAuthSection();
  profileTitle.textContent = '';
  profileDesciption.textContent = '';
  profileAvatar.style.backgroundImage = '';
  loadCards();
  closeModal(logoutModal);
};

// Auth button handler
if (headerAuthButton) {
  headerAuthButton.addEventListener("click", () => {
    hideError(signinError);
    showModal(signinModal);
  });
}

if (headerLogoutButton) {
  headerLogoutButton.addEventListener("click", () => {
    showModal(logoutModal);
  });
}

// Logout confirmation handlers
const confirmLogoutButton = document.getElementById("confirm-logout-button");
const cancelLogoutButton = document.getElementById("cancel-logout-button");

if (confirmLogoutButton) {
  confirmLogoutButton.addEventListener("click", handleLogout);
}

if (cancelLogoutButton) {
  cancelLogoutButton.addEventListener("click", () => {
    closeModal(logoutModal);
  });
}

signinForm.addEventListener("submit", signinFormSubmit);
signupForm.addEventListener("submit", signupFormSubmit);

if (checkAuth()) {
  loadApp();
} else {
  loadCards();
}
