FROM nginx:stable-alpine
LABEL authors="yuraigle"

EXPOSE 80

COPY .output/public /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]
