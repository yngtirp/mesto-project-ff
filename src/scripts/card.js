export const createCard = (nameValue, linkValue, deleteCard, likeButtonState, 
  showPicturePopup, currentLikes, config, 
  cardId, likesArray, ownerId, myId, deleteCardFromServer, likeCard, unlikeCard, isAuthorized = true) => {
  const cardTemplate = document.querySelector("#card-template").content;
  const card = cardTemplate.querySelector(".card").cloneNode(true);
  const deleteButton = card.querySelector(".card__delete-button");
  const likeButton = card.querySelector(".card__like-button");
  const likesCount = card.querySelector(".likes_counter");
  const cardImage = card.querySelector(".card__image");
  card.querySelector(".card__title").textContent = nameValue;

  cardImage.src = linkValue;
  cardImage.alt = `На фото: ${nameValue}`;
  likesCount.textContent = currentLikes

  if (!isAuthorized || ownerId !== myId) {
    deleteButton.remove();
  } else {
    deleteButton.addEventListener("click", () => deleteCard(config, cardId, card, deleteCardFromServer));
  }

  if (isAuthorized && likesArray && likesArray.some(user => user._id === myId)) {
    likeButton.classList.add("card__like-button_is-active");
  }

  if (isAuthorized && config) {
    likeButton.addEventListener("click", () => likeButtonState(likeButton, config, cardId, likesCount, likeCard, unlikeCard));
    likeButton.style.cursor = 'pointer';
  } else {
    likeButton.disabled = true;
    likeButton.style.opacity = '0.5';
    likeButton.style.cursor = 'not-allowed';
  }
  
  cardImage.addEventListener("click", (evt) => showPicturePopup(evt, nameValue));
  
  return card;
}

export const deleteCard = (config, cardId, card, deleteCardFromServer) => { 
  deleteCardFromServer(config, cardId)
  .then(res => {
    card.remove();
  })
  .catch(err => console.log(err))
}

export const likeButtonState = (target, config, cardId, likesCount, likeCard, unlikeCard) => {
  if (!target.classList.contains("card__like-button_is-active")) {
    likeCard(config, cardId)
    .then(data => {
      target.classList.add("card__like-button_is-active");
      likesCount.textContent = data.likes.length;
    })
    .catch(err => console.log(err))
  } else {
    unlikeCard(config, cardId)
    .then(data => {
      target.classList.remove("card__like-button_is-active");
      if (likesCount.textContent === "1") { 
        likesCount.textContent = "";
      } else { 
        likesCount.textContent = data.likes.length;
      }
    })
    .catch(err => console.log(err))
  }
}