import sqlite3

# ----------------------------------------
# CONFIGURACIÓN DE LA BASE DE DATOS
# ----------------------------------------
def inicializar_base_datos():
    """
    Crea la base de datos y las cuentas iniciales si no existen.
    """
    with sqlite3.connect("banco.db") as conn:
        cursor = conn.cursor()
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS cuentas (
                id INTEGER PRIMARY KEY,
                nombre TEXT NOT NULL,
                saldo REAL NOT NULL CHECK(saldo >= 0)
            )
        ''')
        cuentas_iniciales = [
            (1, 'Cliente1', 1000.0),
            (2, 'Cliente2', 500.0)
        ]
        for cuenta in cuentas_iniciales:
            cursor.execute("INSERT OR IGNORE INTO cuentas (id, nombre, saldo) VALUES (?, ?, ?)", cuenta)
        conn.commit()

# ----------------------------------------
# TRANSACCIÓN DE TRANSFERENCIA
# ----------------------------------------
def transferir_fondos(id_origen, id_destino, monto):
    """
    Realiza una transferencia segura entre dos cuentas.
    Esta operación es una transacción: si algo falla, se revierte todo.
    """
    try:
        conn = sqlite3.connect("banco.db")
        cursor = conn.cursor()
        conn.execute('BEGIN')  # Iniciar transacción manualmente

        # Obtener saldo de la cuenta de origen
        cursor.execute("SELECT saldo FROM cuentas WHERE id = ?", (id_origen,))
        resultado = cursor.fetchone()
        if resultado is None:
            raise ValueError("Cuenta de origen no encontrada.")
        saldo_origen = resultado[0]

        if saldo_origen < monto:
            raise ValueError("Fondos insuficientes.")

        # Verificar si la cuenta de destino existe
        cursor.execute("SELECT id FROM cuentas WHERE id = ?", (id_destino,))
        if cursor.fetchone() is None:
            raise ValueError("Cuenta de destino no encontrada.")

        # Realizar la transferencia
        cursor.execute("UPDATE cuentas SET saldo = saldo - ? WHERE id = ?", (monto, id_origen))
        cursor.execute("UPDATE cuentas SET saldo = saldo + ? WHERE id = ?", (monto, id_destino))

        conn.commit()
        print(f"✅ Transferencia de S/.{monto:.2f} realizada exitosamente.")

    except Exception as error:
        conn.rollback()
        print("❌ Error en la transacción:", error)

    finally:
        conn.close()

# ----------------------------------------
# VISUALIZACIÓN DE SALDOS
# ----------------------------------------
def mostrar_saldos():
    """
    Muestra el saldo actual de todas las cuentas en la base de datos.
    """
    with sqlite3.connect("banco.db") as conn:
        cursor = conn.cursor()
        cursor.execute("SELECT nombre, saldo FROM cuentas")
        print("\n📋 Estado actual de las cuentas:")
        for nombre, saldo in cursor.fetchall():
            print(f" - {nombre}: S/.{saldo:.2f}")

# ----------------------------------------
# EJECUCIÓN PRINCIPAL
# ----------------------------------------
if __name__ == "__main__":
    inicializar_base_datos()
    mostrar_saldos()

    print("\n💸 Intentando transferencia de S/.300.00 de Cliente1 a Cliente2...")
    transferir_fondos(1, 2, 300)

    mostrar_saldos()
