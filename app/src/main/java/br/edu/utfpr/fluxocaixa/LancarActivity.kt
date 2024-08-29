package br.edu.utfpr.fluxocaixa

import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.utfpr.fluxocaixa.database.DatabaseFluxoCaixa
import br.edu.utfpr.fluxocaixa.databinding.ActivityLancarBinding
import br.edu.utfpr.fluxocaixa.entity.LancamentoFinanceiro

class LancarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLancarBinding
    private lateinit var database: DatabaseFluxoCaixa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLancarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = DatabaseFluxoCaixa(this)

        spinnerOperacao()

        binding.btSalvar.setOnClickListener {
            if (validaCamposObrigatorios()){
                btSalvarOnClick()
            }
        }

    }

    private fun spinnerOperacao() {
        val listaOperacao = listOf("Débito", "Crédito")

        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, listaOperacao)
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        binding.spOperacao.adapter = adapter

        binding.spOperacao.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val operacaoSelecionada = parent.getItemAtPosition(position).toString()
                spinnerDetalheOperacao(operacaoSelecionada)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Não faz nada
            }
        }

    }

    private fun spinnerDetalheOperacao(operacaoSelecionada: String){

        val listaDebitos = listOf("Alimentação", "Transporte", "Educação", "Lazer", "Saúde", "Outros")
        val listaCreditos = listOf("Salário", "Aluguel", "Investimentos", "Outros")

        val detalhes = when (operacaoSelecionada) {
            "Débito" -> listaDebitos
            "Crédito" -> listaCreditos
            else -> emptyList()
        }

        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, detalhes)
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        binding.spDetalhe.adapter = adapter

    }

    private fun btSalvarOnClick() {

        database.inserirLancamento(
            LancamentoFinanceiro(
                0,
                binding.dpDataLancamento.dayOfMonth.toString() + "/" + binding.dpDataLancamento.month.toString() + "/" + binding.dpDataLancamento.year.toString(),
                binding.etValor.text.toString().toDouble(),
                binding.spOperacao.selectedItem.toString(),
                binding.spDetalhe.selectedItem.toString())
        )

        Toast.makeText(this, "Lançamento realizado com sucesso!", Toast.LENGTH_LONG).show()
    }

    private fun validaCamposObrigatorios() : Boolean{

        desabilitaAvisoErro()

        val valor = binding.etValor.text.toString()
        if(valor.isNullOrEmpty()){
            binding.tilValor.error = "Campo Obrigatório"
            binding.tilValor.requestFocus()
            return false
        }

        return true

    }

    private fun desabilitaAvisoErro(){

        binding.tilValor.error = null
    }

}