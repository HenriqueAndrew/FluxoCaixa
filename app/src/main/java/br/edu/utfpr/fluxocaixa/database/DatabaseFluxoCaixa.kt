package br.edu.utfpr.fluxocaixa.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.edu.utfpr.fluxocaixa.entity.LancamentoFinanceiro

class DatabaseFluxoCaixa(val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, data TEXT, valor REAL, operacao TEXT, detalhe TEXT)")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {

        private const val DATABASE_NAME = "fluxocaixa.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "lancamento"
    }

    fun inserirLancamento(lancamento: LancamentoFinanceiro) {

        val db = this.writableDatabase

        val registro = ContentValues()
        registro.put("data", lancamento.data)
        registro.put("valor", lancamento.valor)
        registro.put("operacao", lancamento.operacao)
        registro.put("detalhe", lancamento.detalhe)

        db.insert(TABLE_NAME, null, registro)

    }

    fun excluirLancamento(_id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "_id = ${_id}", null)

    }

    fun retornarListaLancamentos(): Cursor {

        val db = this.writableDatabase

        val listaLancamentos = db.query(TABLE_NAME, null, null, null, null, null, null)

        return listaLancamentos

    }

    fun consultarSaldo(): String {

        val db = this.writableDatabase

        val cursorCreditos = db.query(
            TABLE_NAME,
            arrayOf("SUM(valor) AS totalCreditos"),
            "OPERACAO = ?",
            arrayOf("Crédito"),
            null,
            null,
            null
        )

        val cursorDebitos = db.query(
            TABLE_NAME,
            arrayOf("SUM(valor) AS totalDebitos"),
            "OPERACAO = ?",
            arrayOf("Débito"),
            null,
            null,
            null
        )

        var totalCreditos = 0.0
        var totalDebitos = 0.0

        if (cursorCreditos.moveToFirst()) {
            totalCreditos = cursorCreditos.getDouble(cursorCreditos.getColumnIndexOrThrow("totalCreditos"))
        }
        cursorCreditos.close()

        if (cursorDebitos.moveToFirst()) {
            totalDebitos = cursorDebitos.getDouble(cursorDebitos.getColumnIndexOrThrow("totalDebitos"))
        }
        cursorDebitos.close()

        val saldo = totalCreditos - totalDebitos

        return String.format("Saldo: R$ %.2f", saldo)


    }


}

