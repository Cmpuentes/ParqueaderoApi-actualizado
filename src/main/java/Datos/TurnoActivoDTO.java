/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

/**
 *
 * @author Carlos Mario
 */
public class TurnoActivoDTO {
    
    private int numeroturno;
    private Long idturno;

    // Constructor vacío (necesario para Jackson)
    public TurnoActivoDTO() {}

    public TurnoActivoDTO(int numeroturno, Long idturno) {
        this.numeroturno = numeroturno;
        this.idturno = idturno;
    }

    public int getNumeroturno() {
        return numeroturno;
    }

    public void setNumeroturno(int numeroturno) {
        this.numeroturno = numeroturno;
    }

    public Long getIdturno() {
        return idturno;
    }

    public void setIdturno(Long idturno) {
        this.idturno = idturno;
    }
}
