# Use the official Nginx image from Docker Hub
FROM nginx:alpine

# Remove the default Nginx HTML files
RUN rm -rf /usr/share/nginx/html/*

# Copy your local HTML, CSS, and JS files to the Nginx web directory
COPY . /usr/share/nginx/html

# Expose port 80 to make the website accessible
EXPOSE 80
