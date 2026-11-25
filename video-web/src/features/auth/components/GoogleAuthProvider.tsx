import type { ReactNode } from 'react'
import { GoogleOAuthProvider } from '@react-oauth/google'

interface GoogleAuthProviderProps {
  children: ReactNode
}

function GoogleAuthProvider({ children }: GoogleAuthProviderProps) {
  const clientId = import.meta.env.VITE_GOOGLE_CLIENT_ID

  if (!clientId) {
    throw new Error('Google Client ID is not configured')
  }

  return (
    <GoogleOAuthProvider clientId={clientId}>
      {children}
    </GoogleOAuthProvider>
  )
}

export default GoogleAuthProvider