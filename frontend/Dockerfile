FROM node:20-alpine AS build
WORKDIR /workspace
COPY package*.json ./
RUN npm ci
COPY . .
ARG VITE_API_BASE=/bff
ARG VITE_AUTH_BASE=/auth
ENV VITE_API_BASE=${VITE_API_BASE}
ENV VITE_AUTH_BASE=${VITE_AUTH_BASE}
RUN npm run build

FROM nginxinc/nginx-unprivileged:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /workspace/dist /usr/share/nginx/html
EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]
