package br.edu.utfpr.fluxocaixa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.utfpr.fluxocaixa.database.DatabaseFluxoCaixa
import br.edu.utfpr.fluxocaixa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseFluxoCaixa
    private var saldoVisivel = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = DatabaseFluxoCaixa(this)

        funcoesAplicacao()
    }


    private fun funcoesAplicacao() {

        binding.btLancar.setOnClickListener {
            val intent = Intent(this, LancarActivity::class.java)
            startActivity(intent)
        }

        binding.btLancamentos.setOnClickListener {
            val intent = Intent(this, ListarLancamentoActivity::class.java)
            startActivity(intent)
        }

        binding.btSaldo.setOnClickListener {
            btSaldoOnClick()
        }

    }

    private fun btSaldoOnClick() {
        if (saldoVisivel){
            binding.tvVerSaldo.text = "Ver Saldo"
            binding.btSaldo.setImageResource(R.drawable.ic_ver_saldo)
            saldoVisivel = false
        } else {
            val saldo = database.consultarSaldo()
            binding.tvVerSaldo.text = saldo
            binding.btSaldo.setImageResource(R.drawable.ic_esconder_saldo)
            saldoVisivel = true
        }


    }


}