Web application showcasing thesis implemented models - Q learning and DQN.

# mongoDB setup
docker pull mongo:4.0.4

At the time of writing this, MongoDB 4.0.4 is the latest version and it is what the latest tag represents. This time we’re actually specifying the 4.0.4 tag because that is what we want.

It may take a moment, but when it is done, we have an image that is ready for deployment.
Deploying an Instance of MongoDB as a Container

With the image available to us, we need to deploy it. In its simplest form, and what is outlined in the Docker Hub documentation, we can execute the following command:

docker run --name mongodb mongo:4.0.4

This command works, but there are potentially a few problems. What we’re doing is we are running a container and naming it mongodb. We aren’t running it in the background and if we wanted to connect to it, we’d have to do it from another container instance. In other words, we wouldn’t be able to connect to it from our host computer or server.

Instead, what we could do is the following:

docker run -d -p 27017-27019:27017-27019 --name mongodb mongo:4.0.4
