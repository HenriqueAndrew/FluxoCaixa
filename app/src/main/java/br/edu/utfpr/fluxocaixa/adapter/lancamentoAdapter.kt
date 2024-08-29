package br.edu.utfpr.fluxocaixa.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import br.edu.utfpr.fluxocaixa.database.DatabaseFluxoCaixa
import br.edu.utfpr.fluxocaixa.databinding.ElementosListaBinding
import br.edu.utfpr.fluxocaixa.entity.LancamentoFinanceiro

class lancamentoAdapter(val context: Context, val cursor: Cursor) : BaseAdapter() {

    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(position: Int): Any {
        cursor.moveToPosition(position)
        val lancamento = LancamentoFinanceiro(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getString(4))
        return lancamento

    }

    override fun getItemId(position: Int): Long {
        cursor.moveToPosition(position)
        return cursor.getLong(0)

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ElementosListaBinding
        val view: View

        if (convertView == null) {
            binding = ElementosListaBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            binding = convertView.tag as ElementosListaBinding
            view = convertView
        }

        cursor.moveToPosition(position)

        val operacao = cursor.getString(3)
        if (operacao == "Crédito") {
            binding.ivCredito.visibility = View.VISIBLE
            binding.ivDebito.visibility = View.GONE
        } else {
            binding.ivCredito.visibility = View.GONE
            binding.ivDebito.visibility = View.VISIBLE
        }

        binding.tvDetalheOperacao.text = cursor.getString(4)
        binding.tvDataLancamento.text = cursor.getString(1)
        val valorFormatado = String.format("R$ %.2f", cursor.getDouble(2))
        binding.tvValorOperacao.text = valorFormatado

        binding.btExcluir.setOnClickListener {
            val db = DatabaseFluxoCaixa(context)
            db.excluirLancamento(cursor.getInt(cursor.getColumnIndexOrThrow("_id")))
            cursor.requery()
            notifyDataSetChanged()

            Toast.makeText(context, "Lançamento excluído com sucesso!", Toast.LENGTH_LONG).show()

        }

        return view
    }

}