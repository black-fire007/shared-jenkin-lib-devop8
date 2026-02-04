#!/bin/bash

# Usage: ./deploy_domain.sh domain_name service_port

domain_name="$1"
service_port="$2"

# Check if running as sudo/root
if [ "$EUID" -ne 0 ]; then 
    echo "Please run as root or use sudo"
    exit 1
fi

# Validate input
if [ -z "$domain_name" ]; then 
    echo "Error: Domain name must not be empty" 
    exit 1 
fi 

if [ -z "$service_port" ]; then 
    echo "Error: service_port must be provided" 
    exit 1 
fi 

# Nginx config directory
NGINX_CONF_DIR="/etc/nginx/conf.d"

# Check if config already exists
if grep -rq "server_name.*${domain_name}" "$NGINX_CONF_DIR"; then
    echo "Reverse proxy config for ${domain_name} already exists. Skipping."
else
    echo "Creating Nginx reverse proxy for ${domain_name} on port ${service_port}..."

    tee "${NGINX_CONF_DIR}/${domain_name}.conf" > /dev/null << EOF
server {
    listen 80;
    listen [::]:80;
    server_name ${domain_name};

    location / {
        proxy_pass http://localhost:${service_port};
        proxy_set_header Host \$http_host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }

    # Optional: error pages
    error_page 404 /index.html;
}
EOF

    echo "Testing Nginx configuration..."
    nginx -t && systemctl reload nginx

    echo "Requesting HTTPS certificate for ${domain_name}..."
    certbot --nginx -d "${domain_name}" --non-interactive --agree-tos -m your-email@example.com
fi

echo "Deployment for ${domain_name} completed!"
