import { beforeEach, describe, expect, test, vi } from 'vitest';
import {
  createCita,
  createListaEspera,
  createPatient,
  fetchAuditEvents,
  fetchAllCitas,
  fetchListaEspera,
  fetchListaEsperaPendiente,
  fetchPatientWithCitas,
  fetchPatients,
  fetchNotifications,
  fetchReassignments,
  loginDoctor,
  markNotificationAsRead,
  reassignAppointment,
  registerDoctor,
  setAuthToken,
  updateListaEsperaEstado
} from './usePatientApi';

function jsonResponse(body, ok = true, status = 200) {
  return {
    ok,
    status,
    json: () => Promise.resolve(body),
    text: () => Promise.resolve(body)
  };
}

describe('usePatientApi', () => {
  beforeEach(() => {
    localStorage.clear();
    global.fetch = vi.fn(() => Promise.resolve(jsonResponse([])));
  });

  test('guarda y envia el token JWT en lecturas protegidas', async () => {
    setAuthToken('jwt-real');
    await fetchPatients();

    expect(fetch).toHaveBeenCalledWith(
      'http://localhost:8085/bff/pacientes',
      expect.objectContaining({
        headers: expect.objectContaining({
          Authorization: 'Bearer jwt-real'
        })
      })
    );
  });

  test('loginDoctor solicita un token al BFF', async () => {
    global.fetch = vi.fn(() => Promise.resolve(jsonResponse({ token: 'abc' })));

    const result = await loginDoctor({
      email: 'medico@redsalud.cl',
      password: 'salud123',
      name: 'Dra. Norte'
    });

    expect(result.token).toBe('abc');
    expect(fetch).toHaveBeenCalledWith(
      'http://localhost:8085/auth/login',
      expect.objectContaining({
        method: 'POST',
        body: expect.stringContaining('medico@redsalud.cl')
      })
    );
  });

  test('registerDoctor crea usuario protegido en el BFF', async () => {
    global.fetch = vi.fn(() => Promise.resolve(jsonResponse({ token: 'nuevo-jwt' })));

    const result = await registerDoctor({
      email: 'nuevo@redsalud.cl',
      password: 'segura123',
      name: 'Dr. Nuevo'
    });

    expect(result.token).toBe('nuevo-jwt');
    expect(fetch).toHaveBeenCalledWith(
      'http://localhost:8085/auth/register',
      expect.objectContaining({ method: 'POST' })
    );
  });

  test('rechaza respuestas HTTP fallidas', async () => {
    global.fetch = vi.fn(() => Promise.resolve(jsonResponse({}, false, 401)));

    await expect(fetchPatients()).rejects.toThrow('Error HTTP 401');
    await expect(loginDoctor({ email: 'x', password: 'bad' })).rejects.toThrow('Credenciales invalidas');
  });

  test('cubre operaciones principales del BFF', async () => {
    global.fetch = vi.fn(() => Promise.resolve(jsonResponse({ ok: true })));
    setAuthToken('jwt-real');

    await fetchPatientWithCitas(9);
    await fetchAllCitas();
    await createPatient({ nombre: 'Sofia' });
    await createCita({ pacienteId: 9 });
    await fetchListaEspera();
    await fetchListaEsperaPendiente();
    await createListaEspera({ pacienteId: 9 });
    await updateListaEsperaEstado(1, 'atender');
    await fetchReassignments();
    await reassignAppointment({ citaId: 5, fechaNueva: '2026-07-01', horaNueva: '12:00' });
    await fetchAuditEvents();
    await fetchNotifications(true);
    await markNotificationAsRead(7);

    expect(fetch).toHaveBeenCalledWith('http://localhost:8085/bff/paciente-citas/9', expect.any(Object));
    expect(fetch).toHaveBeenCalledWith(
      'http://localhost:8085/bff/pacientes',
      expect.objectContaining({ method: 'POST' })
    );
    expect(fetch).toHaveBeenCalledWith(
      'http://localhost:8085/bff/lista-espera/1/atender',
      expect.objectContaining({ method: 'PUT' })
    );
    expect(fetch).toHaveBeenCalledWith(
      'http://localhost:8085/bff/notificaciones/7/leer',
      expect.objectContaining({ method: 'PUT' })
    );
  });

});
