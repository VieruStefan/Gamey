FROM node:22-slim
WORKDIR /app

COPY package*.json ./
COPY yarn.lock ./

EXPOSE 3000
RUN yarn

COPY . .

CMD ["yarn", "run", "dev"]