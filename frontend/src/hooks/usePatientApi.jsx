const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8085/bff';

export async function fetchPatients() {
  const response = await fetch(`${API_BASE}/pacientes`);
  if (!response.ok) {
    throw new Error('Error al obtener pacientes');
  }
  return response.json();
}

export async function fetchPatientWithCitas(patientId) {
  const response = await fetch(`${API_BASE}/paciente-citas/${patientId}`);
  if (!response.ok) {
    throw new Error('Error al obtener datos del paciente');
  }
  return response.json();
}

export async function fetchAllCitas() {
  const response = await fetch(`${API_BASE}/citas`);
  if (!response.ok) {
    throw new Error('Error al obtener citas públicas');
  }
  return response.json();
}

export async function createPatient(patient) {
  const response = await fetch(`${API_BASE}/pacientes`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(patient)
  });

  if (!response.ok) {
    throw new Error('Error al crear paciente');
  }

  return response.json();
}

export async function createCita(cita) {
  const response = await fetch(`${API_BASE}/citas`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(cita)
  });

  if (!response.ok) {
    throw new Error('Error al crear cita');
  }

  return response.json();
}

export async function fetchListaEspera() {
  const response = await fetch(`${API_BASE}/lista-espera`);
  if (!response.ok) {
    throw new Error('Error al obtener lista de espera');
  }
  return response.json();
}

export async function fetchListaEsperaPendiente() {
  const response = await fetch(`${API_BASE}/lista-espera/pendientes`);
  if (!response.ok) {
    throw new Error('Error al obtener lista de espera pendiente');
  }
  return response.json();
}

export async function createListaEspera(registro) {
  const response = await fetch(`${API_BASE}/lista-espera`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(registro)
  });

  if (!response.ok) {
    throw new Error('Error al crear registro de lista de espera');
  }

  return response.json();
}

export async function updateListaEsperaEstado(id, action) {
  const response = await fetch(`${API_BASE}/lista-espera/${id}/${action}`, {
    method: 'PUT'
  });

  if (!response.ok) {
    throw new Error('Error al actualizar lista de espera');
  }

  return response.json();
}

export async function fetchReglasReasignacion() {
  const response = await fetch(`${API_BASE}/reasignacion/reglas`);
  if (!response.ok) {
    throw new Error('Error al obtener reglas de reasignacion');
  }
  return response.json();
}

export async function createReglaReasignacion(regla) {
  const response = await fetch(`${API_BASE}/reasignacion/reglas`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(regla)
  });

  if (!response.ok) {
    throw new Error('Error al crear regla de reasignacion');
  }

  return response.json();
}

export async function inicializarReasignacion() {
  const response = await fetch(`${API_BASE}/reasignacion/inicializar`, {
    method: 'POST'
  });

  if (!response.ok) {
    throw new Error('Error al inicializar reasignacion');
  }

  return response.text();
}

export async function ejecutarReasignacion() {
  const response = await fetch(`${API_BASE}/reasignacion/ejecutar`, {
    method: 'POST'
  });

  if (!response.ok) {
    throw new Error('Error al ejecutar reasignacion');
  }

  return response.text();
}
