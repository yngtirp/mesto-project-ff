const showInputError = (formElement, inputElement, errorMessage, inputErrorClass, errorClass) => { 
  const errorInputText = formElement.querySelector(`.${inputElement.id}-error`);
  errorInputText.textContent = errorMessage;
  inputElement.classList.add(inputErrorClass);
  errorInputText.classList.add(errorClass);
}

const hideInputError = (formElement, inputElement, inputErrorClass, errorClass) => { 
  const errorInputText = formElement.querySelector(`.${inputElement.id}-error`);
  errorInputText.textContent = "";
  inputElement.classList.remove(inputErrorClass);
  errorInputText.classList.remove(errorClass);
  inputElement.setCustomValidity("");
}

export const checkInputValidity = (formElement, inputElement, inputErrorClass, errorClass) => { 
  if (inputElement.validity.patternMismatch) { 
    inputElement.setCustomValidity(inputElement.dataset.errorMessage);
  } else { 
    inputElement.setCustomValidity("");
  }

  if (!inputElement.validity.valid) { 
    showInputError(formElement, inputElement, inputElement.validationMessage, inputErrorClass, errorClass);
  } else { 
    hideInputError(formElement, inputElement, inputErrorClass, errorClass);
  }
}

const hasInvalidInput = (inputList) => { 
  return inputList.some((input) => {
  return !input.validity.valid;
});
}

const disableButton = (buttonElement, inactiveButtonClass) => { 
  buttonElement.classList.add(inactiveButtonClass);
  buttonElement.disabled = true;
}

const enableButton = (buttonElement, inactiveButtonClass) => { 
  buttonElement.classList.remove(inactiveButtonClass);
  buttonElement.disabled = false;
}

export const toggleButtonState = (inputList, buttonElement, inactiveButtonClass) => { 
  if (hasInvalidInput(inputList)) { 
    disableButton(buttonElement, inactiveButtonClass);
  } else { 
    enableButton(buttonElement, inactiveButtonClass);
  }
}

const setEventListeners = (formElement, inputSelector, inactiveButtonClass, submitButtonSelector, inputErrorClass, errorClass) => { 
  const inputList = Array.from(formElement.querySelectorAll(inputSelector));
  const buttonElement = formElement.querySelector(submitButtonSelector)
  toggleButtonState(inputList, buttonElement, inactiveButtonClass);
  inputList.forEach((input) => { 
    input.addEventListener("input", () => {
      checkInputValidity(formElement, input, inputErrorClass, errorClass);
      toggleButtonState(inputList, buttonElement, inactiveButtonClass);
    })
  })
}

export const enableValidation = (validationConfig) => { 
  const formList = Array.from(document.querySelectorAll(validationConfig.formSelector));
  formList.forEach((formElement) => { 
    setEventListeners(formElement, validationConfig.inputSelector, validationConfig.inactiveButtonClass, validationConfig.submitButtonSelector, validationConfig.inputErrorClass, validationConfig.errorClass);
  })
}

export const clearValidation = (formElement, validationConfig) => { 
  const inputList = Array.from(formElement.querySelectorAll(validationConfig.inputSelector));
  const buttonElement = formElement.querySelector(validationConfig.submitButtonSelector); 
  inputList.forEach((inputElement) => { 
    hideInputError(formElement, inputElement, validationConfig.inputErrorClass, validationConfig.errorClass);
  })
  disableButton(buttonElement, validationConfig.inactiveButtonClass);
} 