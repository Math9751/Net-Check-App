# Usa uma imagem oficial leve do Python
FROM python:3.10-slim

# Define a pasta de trabalho dentro do container
WORKDIR /app

# Copia os arquivos do seu computador para dentro do container
COPY server.py .

# Instala o Flask dentro do container
RUN pip install flask

# Avisa que o container vai rodar na porta 8080
EXPOSE 8080

# Comando para iniciar o servidor automaticamente
CMD ["python", "server.py"]