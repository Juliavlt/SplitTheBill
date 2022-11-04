package ifsp.pdm.julia.splitthebill.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Integrante(
    var id: Int,
    var nome: String,
    var valorPago: Double,
    var valorAReceber: Double,
    var itemComprado: String
) : Parcelable
