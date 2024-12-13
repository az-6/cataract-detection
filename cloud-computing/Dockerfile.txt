FROM python:3.9-slim

# Set working directory
WORKDIR /app

# Salin semua file ke container
COPY . /app

# Install dependencies
RUN pip install -r requirements.txt

EXPOSE 8080

CMD ["python", "app.py"]
