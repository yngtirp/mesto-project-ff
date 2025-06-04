let openedModal;

const setOpenedModal = (target) => { 
  openedModal = target;
}

export const showModal = (target) => { 
  setOpenedModal(target);
  target.classList.add("popup_is-opened");
  target.addEventListener("click", listenClickOnModal);
  document.addEventListener("keydown", listenKeyboardOnModal);
}

export const closeModal = (target) => { 
  target.classList.remove("popup_is-opened");
  target.removeEventListener("click", listenClickOnModal);
  document.removeEventListener("keydown", listenKeyboardOnModal);
}

const listenClickOnModal = (evt) => { 
  if (evt.target.classList.contains("popup") || evt.target.classList.contains("popup__close")) {
    closeModal(openedModal);
  }
}

const listenKeyboardOnModal = (evt) => { 
  if (evt.key === "Escape") { 
    closeModal(openedModal);
  }
} 