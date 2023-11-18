import { Alert } from "react-bootstrap";

//prints errors
export default function FormErrors({ errors }) {
  if (!errors || !errors.length) return null;

  if (!Array.isArray(errors)) return null;

  return (
    <Alert variant="warning">
      <h6>The following errors occurred:</h6>
      <ul>
        {errors.map((error) => (
          <li key={error}>{error}</li>
        ))}
      </ul>
    </Alert>
  );
}
