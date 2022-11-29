#
# Build stage
#
FROM node:16-alpine AS build
WORKDIR /app
COPY . .
RUN npm install --loglevel verbose && npm run build

#
# Package stage
#
FROM nginx:1.15
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
