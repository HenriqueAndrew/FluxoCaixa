package br.edu.utfpr.fluxocaixa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.utfpr.fluxocaixa.adapter.lancamentoAdapter
import br.edu.utfpr.fluxocaixa.database.DatabaseFluxoCaixa
import br.edu.utfpr.fluxocaixa.databinding.ActivityListarlancamentosBinding

class ListarLancamentoActivity: AppCompatActivity() {

    private lateinit var binding : ActivityListarlancamentosBinding
    private lateinit var database : DatabaseFluxoCaixa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListarlancamentosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = DatabaseFluxoCaixa(this)

        listarLancamentos()

    }

    private fun listarLancamentos() {

        var listaLancamentos = database.retornarListaLancamentos()
        val adapter = lancamentoAdapter(this, listaLancamentos)
        binding.lvLancamentos.adapter = adapter
    }
}