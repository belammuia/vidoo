export interface GoogleAuthRequest {
  idToken: string
}

export interface AuthUser {
  id: string
  email: string
  fullName: string
  profilePictureUrl: string | null
}

export interface AuthResponse {
  accessToken: string
  tokenType: string
  user: AuthUser
}

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL

export async function authenticateWithGoogle(
  idToken: string,
): Promise<AuthResponse> {
  const response = await fetch(`${API_BASE_URL}/auth/google`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ idToken }),
  })

  if (!response.ok) {
    throw new Error('Google authentication failed')
  }

  return response.json()
}