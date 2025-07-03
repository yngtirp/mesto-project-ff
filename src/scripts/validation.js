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
}

export const checkInputValidity = (formElement, inputElement, inputErrorClass, errorClass, submitButtonSelector) => { 
  const buttonElement = formElement.querySelector(submitButtonSelector);
  if (inputElement.validity.patternMismatch) { 
    inputElement.setCustomValidity(inputElement.dataset.errorMessage);
  } else { 
    inputElement.setCustomValidity("");
  }

  if (!inputElement.validity.valid) { 
    buttonElement.disabled = true;
    showInputError(formElement, inputElement, inputElement.validationMessage, inputErrorClass, errorClass);
  } else { 
    buttonElement.disabled = false;
    hideInputError(formElement, inputElement, inputErrorClass, errorClass);
  }
}

const hasInvalidInput = (inputList) => { 
  return inputList.some((input) => {
  return !input.validity.valid;
});
}

export const toggleButtonState = (inputList, buttonElement, inactiveButtonClass) => { 
  if (hasInvalidInput(inputList)) { 
    buttonElement.classList.add(inactiveButtonClass);
  } else { 
    buttonElement.classList.remove(inactiveButtonClass);
  }
}

const setEventListeners = (formElement, inputSelector, inactiveButtonClass, submitButtonSelector, inputErrorClass, errorClass) => { 
  const inputList = Array.from(formElement.querySelectorAll(inputSelector));
  const buttonElement = formElement.querySelector(submitButtonSelector)
  toggleButtonState(inputList, buttonElement, inactiveButtonClass);
  inputList.forEach((input) => { 
    input.addEventListener("input", () => {
      checkInputValidity(formElement, input, inputErrorClass, errorClass, submitButtonSelector);
      toggleButtonState(inputList, buttonElement, inactiveButtonClass);
    })
  })
}

export const enableValidation = (validationConfig) => { 
  const formList = Array.from(document.querySelectorAll(validationConfig.formSelector));
  formList.forEach((formElement) => { 
    formElement.querySelector(validationConfig.submitButtonSelector).addEventListener("submit", (evt) => {
      evt.preventDefault();
    });
    setEventListeners(formElement, validationConfig.inputSelector, validationConfig.inactiveButtonClass, validationConfig.submitButtonSelector, validationConfig.inputErrorClass, validationConfig.errorClass);
  })
}

export const clearValidation = (formElement, validationConfig) => { 
  const inputList = Array.from(formElement.querySelectorAll(validationConfig.inputSelector));
  const buttonElement = formElement.querySelector(validationConfig.submitButtonSelector);
  inputList.forEach((inputElement) => { 
    const errorElement = formElement.querySelector(`.${inputElement.id}-error`);
    errorElement.textContent = "";
    inputElement.classList.remove(validationConfig.inputErrorClass)
    errorElement.classList.remove(validationConfig.errorClass)
  })
  buttonElement.classList.add(validationConfig.inactiveButtonClass);
  buttonElement.disabled = true;
} 
