import Brand from './Brand'
import GoogleSignInButton from './GoogleSignInButton'
import PrivacyNotice from './PrivacyNotice'

interface AuthCardProps {
  onGoogleSuccess: (idToken: string) => void
  onGoogleError: () => void
  error?: string | null
}

function AuthCard({
  onGoogleSuccess,
  onGoogleError,
  error,
}: AuthCardProps) {
  return (
    <div className="w-full max-w-md rounded-2xl border border-white/10 bg-[#17151f] p-8 shadow-2xl sm:p-10">
      <div className="mb-8 flex justify-center">
        <Brand size="large" />
      </div>

      <div className="mb-8 text-center">
        <p className="mb-4 text-xs font-medium uppercase tracking-[0.25em] text-violet-400">
          Your private screening room
        </p>

        <h1 className="mb-4 text-3xl font-semibold tracking-tight text-white">
          Welcome to Vidoo.
        </h1>

        <p className="text-sm leading-6 text-gray-400">
          Your private space to store videos,
          <br />
          share them and receive feedback.
        </p>
      </div>

      <div className="space-y-6">
        <GoogleSignInButton
          onSuccess={onGoogleSuccess}
          onError={onGoogleError}
        />

        {error && (
          <p role="alert" className="text-center text-sm text-red-400">
            {error}
          </p>
        )}

        <PrivacyNotice />
      </div>
    </div>
  )
}

export default AuthCard
