# STAGE 1: Build-stage
FROM node:18 AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .

# STAGE 2: Run-stage
FROM node:18-alpine
WORKDIR /app
# Copy package files
COPY --from=build-stage /app/package*.json ./

# Copy app.js
COPY --from=build-stage /app/app.js ./


EXPOSE 3000
CMD ["node", "app.js"]