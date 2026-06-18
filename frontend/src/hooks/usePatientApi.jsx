const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8085/bff';
const AUTH_BASE = import.meta.env.VITE_AUTH_BASE || 'http://localhost:8085/auth';
const TOKEN_KEY = 'rednorte-token';

export function setAuthToken(token) {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token);
  } else {
    localStorage.removeItem(TOKEN_KEY);
  }
}

function authHeaders(extraHeaders = {}) {
  const token = localStorage.getItem(TOKEN_KEY);
  return {
    ...extraHeaders,
    ...(token ? { Authorization: `Bearer ${token}` } : {})
  };
}

async function requestJson(url, options = {}) {
  const response = await fetch(url, {
    ...options,
    headers: authHeaders(options.headers)
  });

  if (!response.ok) {
    throw new Error(`Error HTTP ${response.status}`);
  }

  return response.json();
}

export async function loginDoctor(credentials) {
  const response = await fetch(`${AUTH_BASE}/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(credentials)
  });

  if (!response.ok) {
    throw new Error('Credenciales invalidas');
  }

  return response.json();
}

export async function registerDoctor(credentials) {
  const response = await fetch(`${AUTH_BASE}/register`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(credentials)
  });

  if (!response.ok) {
    throw new Error('No se pudo registrar la cuenta');
  }

  return response.json();
}

export async function fetchPatients() {
  return requestJson(`${API_BASE}/pacientes`);
}

export async function fetchPatientWithCitas(patientId) {
  return requestJson(`${API_BASE}/paciente-citas/${patientId}`);
}

export async function fetchAllCitas() {
  return requestJson(`${API_BASE}/citas`);
}

export async function createPatient(patient) {
  return requestJson(`${API_BASE}/pacientes`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(patient)
  });
}

export async function createCita(cita) {
  return requestJson(`${API_BASE}/citas`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(cita)
  });
}

export async function fetchListaEspera() {
  return requestJson(`${API_BASE}/lista-espera`);
}

export async function fetchListaEsperaPendiente() {
  return requestJson(`${API_BASE}/lista-espera/pendientes`);
}

export async function createListaEspera(registro) {
  return requestJson(`${API_BASE}/lista-espera`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(registro)
  });
}

export async function updateListaEsperaEstado(id, action) {
  return requestJson(`${API_BASE}/lista-espera/${id}/${action}`, {
    method: 'PUT'
  });
}

export async function fetchReassignments() {
  return requestJson(`${API_BASE}/reasignaciones`);
}

export async function reassignAppointment(reassignment) {
  return requestJson(`${API_BASE}/reasignaciones`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(reassignment)
  });
}

export async function fetchAuditEvents() {
  return requestJson(`${API_BASE}/auditoria`);
}

export async function fetchNotifications(unreadOnly = false) {
  return requestJson(`${API_BASE}/notificaciones?noLeidas=${unreadOnly}`);
}

export async function markNotificationAsRead(id) {
  return requestJson(`${API_BASE}/notificaciones/${id}/leer`, {
    method: 'PUT'
  });
}
