from flask import Flask, request, jsonify
from datetime import datetime

app = Flask(__name__)

# Endpoint que o aplicativo Android vai chamar via POST
@app.route('/api/report', methods=['POST'])
def receive_report():
    try:
        # Captura o JSON enviado pelo aplicativo Android
        data = request.get_json()
        
        if not data:
            return jsonify({"status": "erro", "mensagem": "JSON inválido ou vazio"}), 400
        
        # Extrai os dados enviados pelo app
        device = data.get('device', 'Desconhecido')
        network = data.get('network', 'Desconhecido')
        timestamp = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        
        # Simula o processamento (exibe no terminal do seu PC)
        print("\n" + "="*40)
        print(f"📥 NOVO RELATÓRIO RECEBIDO - {timestamp}")
        print(f"📱 Dispositivo: {device}")
        print(f"🌐 Conexão Atual: {network}")
        print("="*40 + "\n")
        
        # Retorna uma resposta de sucesso para o Android
        return jsonify({
            "status": "sucesso", 
            "mensagem": "Relatório de conectividade processado com sucesso!"
        }), 200

    except Exception as e:
        return jsonify({"status": "erro", "mensagem": str(e)}), 500

if __name__ == '__main__':
    # Roda o servidor localmente na porta 5000
    # O '0.0.0.0' permite que o celular na mesma rede Wi-Fi consiga acessar seu PC
    app.run(host='0.0.0.0', port=8080, debug=True)