export const API_BASE: string = 'http://localhost:8080/api';

const getTokenFromLocalStorage = (): string => {
  const user: string | null = localStorage.getItem('user')
  return user ? JSON.parse(user).token : ''
}

export const callApi = async (url: string, method: string, body: string | null): Promise<unknown> => {
  return new Promise((resolve, reject) => {
    fetch(API_BASE + url, {
      method,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': getTokenFromLocalStorage(),
      },
      body,
    })
      .then(
        (resp: Response) => {
          resp.json().then(
            (data) => {
              if (resp.ok) {
                resolve(data)
              } else if (data?.messages && data.messages.length > 0) {
                reject(data.messages[0]); // 400/500 response with error dto
              } else if (data?.message) {
                reject(data.message); // default spring boot error
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
