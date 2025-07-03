export const getProfileInfo = (config, profileTitle, profileDesciption, profileAvatar) => {
  return fetch(`${config.baseUrl}/users/me`, {
  headers: config.headers
  })
  .then(res => {
    if (!res.ok) {
      return Promise.reject(`Ошибка при получении данных профиля: ${res.status}`);
    }
    return res.json();
  })
  .then((result) => {
    profileTitle.textContent = result.name;
    profileDesciption.textContent = result.about;
    profileAvatar.style.backgroundImage = `url(${result.avatar})`;
    return result;
  })
  .catch(err => console.log(err));
}

export const getCards = (config) => {
  return fetch(`${config.baseUrl}/cards`, {
  headers: config.headers
  })
  .then(res => {
    if (!res.ok) {
      return Promise.reject(`Ошибка при получении карточек: ${res.status}`);
    }
    return res.json();
  })
  .catch(err => console.log(err));
}

export const updateProfileInfo = (config, profileName, profileDescription) => {
  return fetch(`${config.baseUrl}/users/me`, {
    method: 'PATCH',
    headers: config.headers,
    body: JSON.stringify({
      name: profileName,
      about: profileDescription
    })
  })
  .then(res => {
    if (!res.ok) {
      return Promise.reject(`Ошибка обновления данных профиля: ${res.status}`);
    }
    return res.json();
  })
  .catch(err => console.log(err));
};


export const addNewCardOnServer = (config, cardName, cardLink) => {
  return fetch(`${config.baseUrl}/cards`, {
    method: 'POST',
    headers: config.headers,
    body: JSON.stringify({
      name: cardName,
      link: cardLink
    })
  })
  .then(res => {
    if (!res.ok) {
      return Promise.reject(`Ошибка добавления карточки: ${res.status}`);
    }
    return res.json();
  })
  .catch(err => console.log(err));
};


export const deleteCardFromServer = (config, cardId) => {
  return fetch(`${config.baseUrl}/cards/${cardId}`, {
    method: 'DELETE',
    headers: config.headers
  })
  .then(res => {
    if (!res.ok) {
      return Promise.reject(`Ошибка при удалении карточки: ${res.status}`);
    }
    return res.json();
  })
  .catch(err => console.log(err));
};

export const likeCard = (config, cardId) => {
  return fetch(`${config.baseUrl}/cards//likes/${cardId}`, {
    method: 'PUT',
    headers: config.headers
  })
  .then(res => {
    if (!res.ok) {
      return Promise.reject(`Ошибка установки лайка: ${res.status}`);
    }
    return res.json();
  })
  .catch(err => console.log(err));
};

export const unlikeCard = (config, cardId) => {
  return fetch(`${config.baseUrl}/cards/likes/${cardId}`, {
    method: 'DELETE',
    headers: config.headers
  })
  .then(res => {
    if (!res.ok) {
      return Promise.reject(`Ошибка удаления лайка: ${res.status}`);
    }
    return res.json();
  })
  .catch(err => console.log(err));
};

export const updateAvatar = (config, newAvatarLink) => {
  return fetch(`${config.baseUrl}/users/me/avatar`, {
    method: 'PATCH',
    headers: config.headers,
    body: JSON.stringify({
      avatar: newAvatarLink
    })
  })
  .then(res => {
    if (!res.ok) {
      return Promise.reject(`Ошибка обновления аватара: ${res.status}`);
    }
    return res.json();
  })
  .catch(err => console.log(err));
};