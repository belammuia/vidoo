import { GoogleLogin } from '@react-oauth/google'

interface GoogleSignInButtonProps {
  onSuccess: (idToken: string) => void
  onError: () => void
}

function GoogleSignInButton({
  onSuccess,
  onError,
}: GoogleSignInButtonProps) {
  return (
    <div className="flex w-full justify-center">
      <GoogleLogin
        onSuccess={(credentialResponse) => {
          const idToken = credentialResponse.credential

          if (!idToken) {
            onError()
            return
          }

          onSuccess(idToken)
        }}
        onError={onError}
        text="continue_with"
        theme="filled_black"
        shape="rectangular"
        width="320"
      />
    </div>
  )
}

export default GoogleSignInButton