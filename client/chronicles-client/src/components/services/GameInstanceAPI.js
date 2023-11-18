const LOCAL_STORAGE_TOKEN_KEY = "chronicles-token";
const BASE_URL = "http://localhost:8080";

export async function getGameInstancesByUserId(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(
    `${BASE_URL}/api/game-instance/user/${id}`,
    {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: "Bearer " + jwtToken,
      },
    }
  );

  if (response.status === 200) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

export async function getBox(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(
    `${BASE_URL}/api/board-game/box/${id}`,
    {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: "Bearer " + jwtToken,
      },
    }
  );

  if (response.status === 200) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

export async function getBoardGameById(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(`${BASE_URL}/api/board-game/${id}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
  });

  if (response.status === 200) {
    const data = await response.json();

    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

export async function saveGameInstance(id, saveState) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
    body: JSON.stringify(saveState),
  };

  init.method = "PUT";

  const response = await fetch(
    `${BASE_URL}/api/game-instance/${id}`,
    init
  );

  if (response.status === 204) {
    return;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

export async function getGameInstancesById(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(
    `${BASE_URL}/api/game-instance/${id}`,
    {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: "Bearer " + jwtToken,
      },
    }
  );

  if (response.status === 200) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

export async function getUsersAndTitleByGameInstanceId(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(
    `${BASE_URL}/api/game-instance/users-title/${id}`,
    {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: "Bearer " + jwtToken,
      },
    }
  );

  if (response.status === 200) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

export async function leaveGame(appUserInfo) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
    body: JSON.stringify(appUserInfo),
  };

  init.method = "DELETE";

  const response = await fetch(
    `${BASE_URL}/api/game-instance/user`,
    init
  );

  if (response.status === 204) {
    return;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

export async function addUserToGame(appUserInfo) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
    body: JSON.stringify(appUserInfo),
  };

  init.method = "POST";

  const response = await fetch(
    `${BASE_URL}/api/game-instance/user`,
    init
  );

  if (response.status === 201) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function getUserIdfromUsername(username) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(
    `${BASE_URL}/api/user/username/${username}`,
    {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: "Bearer " + jwtToken,
      },
    }
  );
  if (response.status === 200) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function getBoardGames() {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(`${BASE_URL}/api/board-game`, {
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
  });

  if (response.status === 200) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

export async function createGameInstance(gameInstanceInfo) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
    body: JSON.stringify(gameInstanceInfo),
  };

  init.method = "POST";

  const response = await fetch(`${BASE_URL}/api/game-instance`, init);

  if (response.status === 201) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function getUserInfoFromId(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(`${BASE_URL}/api/user/${id}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
  });

  if (response.status === 200) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

export async function updateColorAndAvatar(colorAndAvatar, id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
    body: JSON.stringify(colorAndAvatar),
  };

  init.method = "PUT";

  const response = await fetch(
    `${BASE_URL}/api/user/color/${id}`,
    init
  );

  if (response.status === 204) {
    return;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function createBoardGame(boardGame) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
    body: JSON.stringify(boardGame),
  };

  init.method = "POST";

  const response = await fetch(`${BASE_URL}/api/board-game`, init);

  if (response.status === 201) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

//------------------------------Boards----------------------------------//

export async function getBoards(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(
    `${BASE_URL}/api/board/board-game/${id}`,
    {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: "Bearer " + jwtToken,
      },
    }
  );

  if (response.status === 200) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

export async function createBoard(board) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
    body: JSON.stringify(board),
  };

  init.method = "POST";

  const response = await fetch(`${BASE_URL}/api/board`, init);

  if (response.status === 201) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function updateBoard(board, id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
    body: JSON.stringify(board),
  };

  init.method = "PUT";

  const response = await fetch(`${BASE_URL}/api/board/${id}`, init);

  if (response.status === 204) {
    return;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function deleteBoard(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
  };

  init.method = "DELETE";

  const response = await fetch(`${BASE_URL}/api/board/${id}`, init);

  if (response.status === 204) {
    return;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function getBoard(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(`${BASE_URL}/api/board/${id}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
  });

  if (response.status === 200) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

//------------------------------Figures----------------------------------//

export async function getFigures(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(
    `${BASE_URL}/api/figure/board-game/${id}`,
    {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: "Bearer " + jwtToken,
      },
    }
  );

  if (response.status === 200) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

export async function createFigure(board) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
    body: JSON.stringify(board),
  };

  init.method = "POST";

  const response = await fetch(`${BASE_URL}/api/figure`, init);

  if (response.status === 201) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function updateFigure(board, id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
    body: JSON.stringify(board),
  };

  init.method = "PUT";

  const response = await fetch(`${BASE_URL}/api/figure/${id}`, init);

  if (response.status === 204) {
    return;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function deleteFigure(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
  };

  init.method = "DELETE";

  const response = await fetch(`${BASE_URL}/api/figure/${id}`, init);

  if (response.status === 204) {
    return;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function getFigure(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(`${BASE_URL}/api/figure/${id}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
  });

  if (response.status === 200) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

//------------------------------Cards----------------------------------//

export async function getCards(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(
    `${BASE_URL}/api/card/board-game/${id}`,
    {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: "Bearer " + jwtToken,
      },
    }
  );

  if (response.status === 200) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

export async function createCard(board) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
    body: JSON.stringify(board),
  };

  init.method = "POST";

  const response = await fetch(`${BASE_URL}/api/card`, init);

  if (response.status === 201) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function updateCard(board, id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
    body: JSON.stringify(board),
  };

  init.method = "PUT";

  const response = await fetch(`${BASE_URL}/api/card/${id}`, init);

  if (response.status === 204) {
    return;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function deleteCard(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
  };

  init.method = "DELETE";

  const response = await fetch(`${BASE_URL}/api/card/${id}`, init);

  if (response.status === 204) {
    return;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function getCard(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(`${BASE_URL}/api/card/${id}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
  });

  if (response.status === 200) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

//------------------------------Dice----------------------------------//

export async function getDice(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(
    `${BASE_URL}/api/die/board-game/${id}`,
    {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: "Bearer " + jwtToken,
      },
    }
  );

  if (response.status === 200) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

export async function createDie(board) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
    body: JSON.stringify(board),
  };

  init.method = "POST";

  const response = await fetch(`${BASE_URL}/api/die`, init);

  if (response.status === 201) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function updateDie(board, id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
    body: JSON.stringify(board),
  };

  init.method = "PUT";

  const response = await fetch(`${BASE_URL}/api/die/${id}`, init);

  if (response.status === 204) {
    return;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function deleteDie(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }

  const init = {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
  };

  init.method = "DELETE";

  const response = await fetch(`${BASE_URL}/api/die/${id}`, init);

  if (response.status === 204) {
    return;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const data = await response.json();
    return Promise.reject(data);
  }
}

export async function getDie(id) {
  const jwtToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (!jwtToken) {
    return Promise.reject("Unauthorized.");
  }
  const response = await fetch(`${BASE_URL}/api/die/${id}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwtToken,
    },
  });

  if (response.status === 200) {
    const data = await response.json();
    return data;
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden access. Please log in and try again"]);
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}
