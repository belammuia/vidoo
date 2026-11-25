
import { useState } from 'react'
import AuthCard from '../components/AuthCard'
import Brand from '../components/Brand'
import { authenticateWithGoogle } from '../services/authService'

function LoginPage() {
  const [error, setError] = useState<string | null>(null)

  const handleGoogleSuccess = async (idToken: string) => {
    setError(null)

    try {
      const response = await authenticateWithGoogle(idToken)

      console.log('Authentication successful:', response)
    } catch {
      setError('Unable to sign in. Please try again.')
    }
  }

  const handleGoogleError = () => {
    setError('Google sign-in was unsuccessful. Please try again.')
  }

  return (
    <main
      className="relative flex min-h-screen items-center justify-center overflow-hidden bg-cover bg-center px-5 py-16"
      style={{
        backgroundImage: "url('/images/auth-background.jpg')",
      }}
    >
      <div className="absolute inset-0 bg-black/65 backdrop-blur-[3px]" />

      <header className="absolute left-10 top-8 z-10">
        <Brand size="small" />
      </header>

      <section className="relative z-10 w-full max-w-md">
        <AuthCard
          onGoogleSuccess={handleGoogleSuccess}
          onGoogleError={handleGoogleError}
          error={error}
        />
      </section>
    </main>
  )
}

export default LoginPage
