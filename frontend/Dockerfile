FROM node:22-slim
WORKDIR /app

COPY package*.json ./
COPY yarn.lock ./
RUN apt-get update
RUN apt-get install curl -y

EXPOSE 3000
RUN yarn

COPY . .

CMD ["yarn", "run", "dev"]