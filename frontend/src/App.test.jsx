import { cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react';
import { afterEach, beforeEach, expect, test, vi } from 'vitest';
import App from './App';
import * as api from './hooks/usePatientApi';

vi.mock('./hooks/usePatientApi', () => ({
  setAuthToken: vi.fn(),
  loginDoctor: vi.fn(),
  registerDoctor: vi.fn(),
  fetchPatients: vi.fn(),
  fetchPatientWithCitas: vi.fn(),
  fetchAllCitas: vi.fn(),
  createPatient: vi.fn(),
  createCita: vi.fn(),
  fetchListaEspera: vi.fn(),
  fetchListaEsperaPendiente: vi.fn(),
  createListaEspera: vi.fn(),
  updateListaEsperaEstado: vi.fn(),
  fetchReassignments: vi.fn(),
  reassignAppointment: vi.fn(),
  fetchAuditEvents: vi.fn(),
  fetchNotifications: vi.fn(),
  markNotificationAsRead: vi.fn()
}));

const patient = {
  id: 1,
  nombre: 'Ana',
  apellido: 'Lopez',
  rut: '11111111-1',
  historialClinico: 'Control anual'
};

beforeEach(() => {
  localStorage.clear();
  vi.clearAllMocks();
  api.fetchPatients.mockResolvedValue([patient]);
  api.fetchPatientWithCitas.mockResolvedValue({
    paciente: patient,
    citas: [{ id: 7, pacienteId: 1, fecha: '2026-07-01', hora: '10:00' }]
  });
  api.createPatient.mockResolvedValue({ ...patient, id: 2 });
  api.fetchAllCitas.mockResolvedValue([]);
  api.fetchListaEsperaPendiente.mockResolvedValue([]);
  api.fetchReassignments.mockResolvedValue([]);
  api.fetchAuditEvents.mockResolvedValue([]);
  api.fetchNotifications.mockResolvedValue([]);
  window.alert = vi.fn();
});

afterEach(() => cleanup());

test('muestra el acceso privado cuando no hay sesion', () => {
  render(<App />);
  expect(screen.getByText(/Panel clinico Hospital Red Norte/i)).toBeTruthy();
  expect(screen.getByText(/Registrarse/i)).toBeTruthy();
});

test('inicia sesion desde el formulario y conserva el JWT', async () => {
  api.loginDoctor.mockResolvedValue({
    token: 'jwt-desde-login',
    name: 'Dra. Ana',
    email: 'ana@redsalud.cl',
    role: 'medico',
    registeredUsers: 2
  });

  render(<App />);
  fireEvent.change(screen.getByLabelText(/Correo institucional/i), {
    target: { value: 'ana@redsalud.cl' }
  });
  fireEvent.change(screen.getByLabelText(/Contrasena/i), {
    target: { value: 'segura123' }
  });
  fireEvent.click(screen.getByRole('button', { name: /Entrar al panel/i }));

  await waitFor(() => expect(screen.getByText(/Sesion medica activa/i)).toBeTruthy());
  expect(api.setAuthToken).toHaveBeenCalledWith('jwt-desde-login');
  expect(localStorage.getItem('rednorte-user')).toContain('jwt-desde-login');
});

test('ejecuta el flujo clinico y navega por todos los paneles', async () => {
  localStorage.setItem('rednorte-user', JSON.stringify({
    token: 'jwt-firmado',
    name: 'Dra. Ana',
    email: 'ana@redsalud.cl'
  }));
  localStorage.setItem('rednorte-settings', JSON.stringify({
    compactMode: true,
    highContrast: true
  }));

  const { container } = render(<App />);

  await waitFor(() => expect(screen.getByText('Ana Lopez')).toBeTruthy());
  expect(container.querySelector('.app-shell').className).toContain('compact-mode');
  expect(api.setAuthToken).toHaveBeenCalledWith('jwt-firmado');

  fireEvent.change(screen.getByPlaceholderText(/Buscar por nombre o RUT/i), {
    target: { value: '11111111-1' }
  });
  fireEvent.click(screen.getByText('Ana Lopez'));
  await waitFor(() => expect(screen.getByText(/Control anual/i)).toBeTruthy());

  fireEvent.change(screen.getByPlaceholderText('12345678-9'), {
    target: { value: '22222222-2' }
  });
  fireEvent.change(screen.getByPlaceholderText('Nombre'), { target: { value: 'Luis' } });
  fireEvent.change(screen.getByPlaceholderText('Apellido'), { target: { value: 'Perez' } });
  fireEvent.click(screen.getByRole('button', { name: /Agregar paciente/i }));
  await waitFor(() => expect(api.createPatient).toHaveBeenCalled());
  expect(screen.getByText(/Paciente agregado correctamente/i)).toBeTruthy();

  fireEvent.click(screen.getByRole('button', { name: /Citas/i }));
  await waitFor(() => expect(screen.getByRole('heading', { name: /Agendar cita/i })).toBeTruthy());

  fireEvent.click(screen.getByRole('button', { name: /Lista de espera/i }));
  await waitFor(() => expect(screen.getByText(/Agregar a lista de espera/i)).toBeTruthy());

  fireEvent.click(screen.getByRole('button', { name: /Reportes/i }));
  expect(screen.getByText(/Resumen del turno/i)).toBeTruthy();

  fireEvent.click(screen.getByRole('button', { name: /Configuracion/i }));
  expect(screen.getByRole('heading', { name: /Configuracion del panel/i })).toBeTruthy();
  fireEvent.click(screen.getByText(/Vista compacta/i));
  fireEvent.click(screen.getByText(/Mostrar notificaciones/i));
  fireEvent.click(screen.getByText(/Actualizacion automatica/i));
  fireEvent.click(screen.getByText(/Contraste reforzado/i));

  fireEvent.click(screen.getByRole('button', { name: /Cerrar sesion/i }));
  expect(screen.getByText(/Panel clinico Hospital Red Norte/i)).toBeTruthy();
  expect(api.setAuthToken).toHaveBeenCalledWith(null);
});

test('informa fallos al cargar, seleccionar y crear pacientes', async () => {
  localStorage.setItem('rednorte-user', JSON.stringify({ token: 'jwt', name: 'Dra. Ana' }));
  api.fetchPatients.mockRejectedValueOnce(new Error('sin conexion'));

  render(<App />);
  await waitFor(() => expect(screen.getByText(/No se pudo cargar la lista/i)).toBeTruthy());

  api.fetchPatients.mockResolvedValue([patient]);
  fireEvent.click(screen.getByRole('button', { name: /Reportes/i }));
  fireEvent.click(screen.getByRole('button', { name: /^Pacientes$/i }));
  await waitFor(() => expect(screen.getByText('Ana Lopez')).toBeTruthy());

  api.fetchPatientWithCitas.mockRejectedValueOnce(new Error('detalle'));
  fireEvent.click(screen.getByText('Ana Lopez'));
  await waitFor(() => expect(screen.getByText(/Control anual/i)).toBeTruthy());
  expect(screen.getByText(/Sin citas registradas/i)).toBeTruthy();

  api.createPatient.mockRejectedValueOnce(new Error('guardar'));
  fireEvent.change(screen.getByPlaceholderText('12345678-9'), {
    target: { value: '22222222-2' }
  });
  fireEvent.change(screen.getByPlaceholderText('Nombre'), { target: { value: 'Luis' } });
  fireEvent.change(screen.getByPlaceholderText('Apellido'), { target: { value: 'Perez' } });
  fireEvent.click(screen.getByRole('button', { name: /Agregar paciente/i }));
  await waitFor(() => expect(screen.getByText(/No se pudo agregar el paciente/i)).toBeTruthy());
});

test('mantiene el paciente creado aunque falle la recarga de datos relacionados', async () => {
  localStorage.setItem('rednorte-user', JSON.stringify({ token: 'jwt', name: 'Dra. Ana' }));
  const createdPatient = {
    id: 2,
    nombre: 'Luis',
    apellido: 'Perez',
    rut: '22222222-2',
    historialClinico: 'Ingreso inicial'
  };
  api.fetchPatients.mockResolvedValueOnce([]);
  api.fetchPatients.mockRejectedValueOnce(new Error('recarga'));
  api.fetchPatientWithCitas.mockRejectedValue(new Error('citas apagado'));
  api.createPatient.mockResolvedValueOnce(createdPatient);

  render(<App />);
  await waitFor(() => expect(screen.getByText(/No hay pacientes disponibles/i)).toBeTruthy());

  fireEvent.change(screen.getByPlaceholderText('12345678-9'), {
    target: { value: createdPatient.rut }
  });
  fireEvent.change(screen.getByPlaceholderText('Nombre'), { target: { value: createdPatient.nombre } });
  fireEvent.change(screen.getByPlaceholderText('Apellido'), { target: { value: createdPatient.apellido } });
  fireEvent.click(screen.getByRole('button', { name: /Agregar paciente/i }));

  await waitFor(() => expect(screen.getByText(/Paciente agregado correctamente/i)).toBeTruthy());
  expect(screen.getAllByText(/Luis Perez/i).length).toBeGreaterThan(0);
  expect(api.fetchPatientWithCitas).not.toHaveBeenCalled();
});
