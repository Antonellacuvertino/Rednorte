import { useState } from 'react';

const initialForm = {
  rut: '',
  nombre: '',
  apellido: '',
  historialClinico: ''
};

function PatientForm({ onSubmit, saving }) {
  const [form, setForm] = useState(initialForm);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      await onSubmit(form);
      setForm(initialForm);
    } catch {
      // El contenedor muestra el error; conservamos los datos para corregir o reintentar.
    }
  };

  return (
    <form className="patient-form" onSubmit={handleSubmit}>
      <div className="form-grid">
        <label className="field">
          <span>RUT</span>
          <input
            name="rut"
            value={form.rut}
            onChange={handleChange}
            placeholder="12345678-9"
            required
          />
        </label>

        <label className="field">
          <span>Nombre</span>
          <input
            name="nombre"
            value={form.nombre}
            onChange={handleChange}
            placeholder="Nombre"
            required
          />
        </label>

        <label className="field">
          <span>Apellido</span>
          <input
            name="apellido"
            value={form.apellido}
            onChange={handleChange}
            placeholder="Apellido"
            required
          />
        </label>

        <label className="field field-wide">
          <span>Historial clinico</span>
          <textarea
            name="historialClinico"
            value={form.historialClinico}
            onChange={handleChange}
            placeholder="Antecedentes relevantes, alergias o notas clinicas"
            rows="3"
          />
        </label>
      </div>

      <button className="primary-button" type="submit" disabled={saving}>
        {saving ? 'Guardando...' : 'Agregar paciente'}
      </button>
    </form>
  );
}

export default PatientForm;
