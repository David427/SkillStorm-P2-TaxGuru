# using bun (https://bun.sh) as a package manager, a faster npm alternative
FROM oven/bun:1

# create and set our working dir to /app
WORKDIR /app

# copy our package.json to /app
COPY package.json .

# install our packages
RUN bun install

# copy the rest of the project files
COPY . .

# build for production using vite build
RUN bun run build

EXPOSE 4173

CMD ["bun", "run", "preview"]
