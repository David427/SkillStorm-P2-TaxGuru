import {
  Form,
  Grid,
  Label,
  Button,
  Select,
  Fieldset,
  TextInput,
  GridContainer,
  StepIndicator,
  TextInputMask,
  StepIndicatorStep,
} from "@trussworks/react-uswds";
import { FormEvent } from "react";

import { states } from "../../states";

import { useNavigate } from "react-router-dom";

export default function Personal() {
  const navigate = useNavigate();

  const handlePersonalInfo = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      firstName: e.currentTarget.elements.first_name.value,
      // @ts-expect-error untyped form elements but we need the values
      lastName: e.currentTarget.elements.last_name.value,
      // @ts-expect-error untyped form elements but we need the values
      birthdate: e.currentTarget.elements.birthdate.value,
      // @ts-expect-error untyped form elements but we need the values
      tel: e.currentTarget.elements.tel.value,
      // @ts-expect-error untyped form elements but we need the values
      streetAddress: e.currentTarget.elements.street_address.value,
      // @ts-expect-error untyped form elements but we need the values
      city: e.currentTarget.elements.city.value,
      // @ts-expect-error untyped form elements but we need the values
      state: e.currentTarget.elements.state.value,
      // @ts-expect-error untyped form elements but we need the values
      zipcode: e.currentTarget.elements.zipcode.value,
      // @ts-expect-error untyped form elements but we need the values
      filingStatus: e.currentTarget.elements.filing_status.value,
    };

    console.log(formData);

    navigate("/filing/w2");
  };

  return (
    <main className="full-page">
      <GridContainer className="usa-section">
        <StepIndicator headingLevel="h4" ofText="of" stepText="Step">
          <StepIndicatorStep label="Personal Information" status="current" />
          <StepIndicatorStep label="W-2 Information" />
          <StepIndicatorStep label="Self Employment" />
          <StepIndicatorStep label="Deductions" />
          <StepIndicatorStep label="Review" />
          <StepIndicatorStep label="Results" />
        </StepIndicator>

        <Form onSubmit={handlePersonalInfo} className="w-full">
          <Fieldset legend="Enter your personal information.">
            {/* First and Last Name */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="first_name">First Name</Label>
                <TextInput
                  id="first_name"
                  name="first_name"
                  type="text"
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label htmlFor="last_name">Last Name</Label>
                <TextInput
                  id="last_name"
                  name="last_name"
                  type="text"
                  required
                />
              </Grid>
            </Grid>

            {/* Birthdate and Phone Number */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label id="label-birthdate" htmlFor="birthdate">
                  Birthdate
                </Label>
                <span id="hint-birthdate" className="usa-hint">
                  MM/DD/YYYY
                </span>
                <TextInputMask
                  id="birthdate"
                  name="birthdate"
                  type="text"
                  aria-labelledby="label-birthdate"
                  aria-describedby="hint-birthdate"
                  mask="__/__/____"
                  // the pattern fits the date format MM/DD/YYYY
                  pattern="^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\d{4}$"
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label id="label-tel" htmlFor="tel">
                  US Telephone Number
                </Label>
                <span id="hint-tel" className="usa-hint">
                  123-456-7890
                </span>
                <TextInputMask
                  id="tel"
                  name="tel"
                  type="text"
                  aria-labelledby="label-tel"
                  aria-describedby="hint-tel"
                  mask="___-___-____"
                  pattern="\d{3}-\d{3}-\d{4}"
                  required
                />
              </Grid>
            </Grid>

            {/* Street Address and City */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="street_address">Street Address</Label>
                <TextInput
                  id="street_address"
                  name="street_address"
                  type="text"
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label htmlFor="city">City</Label>
                <TextInput id="city" name="city" type="text" required />
              </Grid>
            </Grid>

            {/* State and Filing Status */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="state">State</Label>
                <Select id="state" name="state" required>
                  <option>- Select -</option>
                  {states.map((s) => (
                    <option key={s} value={s}>
                      {s}
                    </option>
                  ))}
                </Select>
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label htmlFor="zipcode">Zip Code</Label>
                <TextInputMask
                  id="zipcode"
                  name="zipcode"
                  type="text"
                  mask="_____"
                  pattern="^\d{5}"
                  required
                />
              </Grid>
            </Grid>

            <Grid row>
              <Grid tablet={{ col: 6 }}>
                <Label htmlFor="filing_status">Filing Status</Label>
                <Select id="filing_status" name="filing_status" required>
                  <option>- Select -</option>
                  <option value="single">Single</option>
                  <option value="married_filing_jointly">
                    Married filing jointly
                  </option>
                  <option value="married_filing_separately">
                    Married filing separately
                  </option>
                  <option value="head_of_household">Head of household</option>
                </Select>
              </Grid>
            </Grid>

            <Button type="submit">Continue</Button>
          </Fieldset>
        </Form>
      </GridContainer>
    </main>
  );
}
