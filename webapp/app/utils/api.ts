const API_URL = 'http://localhost:8080/api';

const getTokenFromLocalStorage = (): string => {
  const user: string | null = localStorage.getItem('user')
  return user ? JSON.parse(user).token : ''
}

export const callApi = async (url: string, method: string, body: string | null): Promise<any> => {
  return new Promise((resolve, reject) => {
    fetch(API_URL + url, {
      method: method,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': getTokenFromLocalStorage(),
      },
      body: body,
    })
      .then(
        (resp) => {
          resp.json().then(
            (data) => {
              if (resp.ok) {
                resolve(data)
              } else if (data?.messages && data.messages.length > 0) {
                reject(data.messages[0]); // 400/500 with errors object
              } else {
                reject('Error ' + resp.status); // some other error
              }
            },
            () => reject('Error reading API response')
          )
        },
        () => reject('Error connecting to API')
      )
  });
}
