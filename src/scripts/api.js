const getResponseData = (res) => {
  if (!res.ok) {
      return Promise.reject(`Ошибка: ${res.status}`); 
  }
  return res.json();
}

export const getProfileInfo = (config) => {
  return fetch(`${config.baseUrl}/users/me`, {
  headers: config.headers
  })
  .then(getResponseData);
}

export const getCards = (config) => {
  return fetch(`${config.baseUrl}/cards`, {
  headers: config.headers
  })
  .then(getResponseData);
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
  .then(getResponseData);
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
  .then(getResponseData);
};


export const deleteCardFromServer = (config, cardId) => {
  return fetch(`${config.baseUrl}/cards/${cardId}`, {
    method: 'DELETE',
    headers: config.headers
  })
  .then(getResponseData);
};

export const likeCard = (config, cardId) => {
  return fetch(`${config.baseUrl}/cards/likes/${cardId}`, {
    method: 'PUT',
    headers: config.headers
  })
  .then(getResponseData);
};

export const unlikeCard = (config, cardId) => {
  return fetch(`${config.baseUrl}/cards/likes/${cardId}`, {
    method: 'DELETE',
    headers: config.headers
  })
  .then(getResponseData);
};

export const updateAvatar = (config, newAvatarLink) => {
  return fetch(`${config.baseUrl}/users/me/avatar`, {
    method: 'PATCH',
    headers: config.headers,
    body: JSON.stringify({
      avatar: newAvatarLink
    })
  })
  .then(getResponseData);
};

export const signup = (config, email, password, name, about, avatar) => {
  const body = {
    email: email,
    password: password,
    name: name
  };
  if (about) body.about = about;
  if (avatar) body.avatar = avatar;
  
  return fetch(`${config.baseUrl}/signup`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(body)
  })
  .then(getResponseData);
};

export const signin = (config, email, password) => {
  return fetch(`${config.baseUrl}/signin`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      email: email,
      password: password
    })
  })
  .then(getResponseData);
};