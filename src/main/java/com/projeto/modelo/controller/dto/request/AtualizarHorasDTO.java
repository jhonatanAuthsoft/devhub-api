import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record AtualizarHorasDTO(
    @JsonProperty("horasPrevistas") BigDecimal horasPrevistas
) {}
